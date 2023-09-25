package com.conseller.conseller.store;

import com.conseller.conseller.category.mainCategory.MainCategoryRepository;
import com.conseller.conseller.category.subCategory.SubCategoryRepository;
import com.conseller.conseller.entity.Gifticon;
import com.conseller.conseller.entity.Store;
import com.conseller.conseller.entity.User;
import com.conseller.conseller.gifticon.GifticonRepository;
import com.conseller.conseller.gifticon.enums.GifticonStatus;
import com.conseller.conseller.store.dto.mapper.StoreMapper;
import com.conseller.conseller.store.dto.request.ModifyStoreRequest;
import com.conseller.conseller.store.dto.request.RegistStoreRequest;
import com.conseller.conseller.store.dto.request.StoreListRequest;
import com.conseller.conseller.store.dto.response.DetailStoreResponse;
import com.conseller.conseller.store.dto.response.StoreItemData;
import com.conseller.conseller.store.dto.response.StoreListResponse;
import com.conseller.conseller.store.dto.response.StoreTradeResponse;
import com.conseller.conseller.store.enums.StoreStatus;
import com.conseller.conseller.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final GifticonRepository gifticonRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final StoreRepositoryImpl storeRepositoryImpl;

    // 스토어 목록
    @Transactional(readOnly = true)
    public StoreListResponse getStoreList(StoreListRequest request) { //queryDSL 사용
        Pageable pageable = PageRequest.of(request.getPage(), 10);

        Page<Store> stores = storeRepositoryImpl.findStoreList(request, pageable);

        List<StoreItemData> storeItemDataList = StoreMapper.INSTANCE.storesToItemDatas(stores.getContent());

        StoreListResponse response = new StoreListResponse(storeItemDataList,
                stores.getTotalElements(),
                stores.getTotalPages());

        return response;
    }

    // 스토어 글 등록
    public void registStore(RegistStoreRequest request) {

        User user = userRepository.findById(request.getUserIdx())
                .orElseThrow(() -> new RuntimeException());
        Gifticon gifticon = gifticonRepository.findById(request.getGifticonIdx())
                .orElseThrow(() -> new RuntimeException());

        if(!gifticon.getGifticonStatus().equals(GifticonStatus.KEEP.getStatus())){

        }else {
            Store store = StoreMapper.INSTANCE.registStoreRequestToStore(request, user, gifticon);

            gifticon.setGifticonStatus(GifticonStatus.STORE.getStatus());

            storeRepository.save(store);
        }
    }

    // 스토어 글 상세보기
    @Transactional(readOnly = true)
    public DetailStoreResponse detailStore(Long storeIdx) {
        Store store = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new RuntimeException());

        User user = userRepository.findById(store.getUser().getUserIdx())
                .orElseThrow(() -> new RuntimeException());

        DetailStoreResponse response = StoreMapper.INSTANCE.entityToDetailStoreResponse(user, store);

        return response;
    }

    //스토어 글 수정
    public void modifyStore(Long storeIdx , ModifyStoreRequest storeRequest) {
        Store store = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new RuntimeException());

        store.setStoreEndDate(storeRequest.getStoreEndDate());
        store.setStoreText(storeRequest.getStoreText());
    }

    // 스토어 글 삭제
    public void deleteStore(Long storeIdx) {
        storeRepository.deleteById(storeIdx);
    }

    // 스토어 거래 진행
    @Override
    public StoreTradeResponse tradeStore(Long storeIdx, Long consumerIdx) {
        Store store = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new RuntimeException());
        User consumer = userRepository.findById(consumerIdx)
                .orElseThrow(() -> new RuntimeException());

        // 판매자에게 알림

        // 구매자의 인덱스를 저장
        store.setConsumer(consumer);
        
        // 거래 상태 거래중으로 변경
        store.setStoreStatus(StoreStatus.IN_TRADE.getStatus());

        StoreTradeResponse response = new StoreTradeResponse(store.getUser().getUserAccount(),
                store.getUser().getUserAccountBank());

        return response;
    }

    // 스토어 거래 취소
    @Override
    public void cancelStore(Long storeIdx) {
        Store store = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new RuntimeException());

        // 거래 취소 알림

        // 거래 상태를 진행중으로 변경
        store.setStoreStatus(StoreStatus.IN_PROGRESS.getStatus());

        // 구매자 정보 삭제
        store.setConsumer(null);
    }

    // 스토어 입금 완료
    @Override
    public void confirmStore(Long storeIdx) {
        // 판매자에게 알림
    }

    // 스토어 입금 확인
    @Override
    public void completeStore(Long storeIdx) {
        Store store = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new RuntimeException());
        Gifticon gifticon = gifticonRepository.findById(store.getGifticon().getGifticonIdx())
                .orElseThrow(() -> new RuntimeException());
        User user = userRepository.findById(store.getConsumer().getUserIdx())
                .orElseThrow(() -> new RuntimeException());

        store.setStoreStatus(StoreStatus.AWARDED.getStatus());

        gifticon.setUser(user);
        
        // 알림
    }

}