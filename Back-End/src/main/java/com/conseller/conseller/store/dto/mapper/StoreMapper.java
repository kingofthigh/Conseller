package com.conseller.conseller.store.dto.mapper;

import com.conseller.conseller.entity.Gifticon;
import com.conseller.conseller.entity.Store;
import com.conseller.conseller.entity.User;
import com.conseller.conseller.store.dto.request.RegistStoreRequest;
import com.conseller.conseller.store.dto.response.DetailStoreResponse;
import com.conseller.conseller.store.dto.response.StoreItemData;
import com.conseller.conseller.utils.DateTimeConverter;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel="spring")
public interface StoreMapper {
    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    //RegistSaleRequest -> Store 매핑
    @Mapping(source = "user", target = "user")
    @Mapping(source = "gifticon", target = "gifticon")
    Store registStoreRequestToStore(RegistStoreRequest request, User user, Gifticon gifticon);

    //User, Store -> DetailStoreResponse 매핑
    default DetailStoreResponse entityToDetailStoreResponse(Store store) {
        DetailStoreResponse response = new DetailStoreResponse();

        response.setPostContent(store.getStoreText());
        response.setStoreUserIdx(store.getUser().getUserIdx());
        response.setStoreUserNickname(store.getUser().getUserNickname());
        response.setStoreUserProfileUrl(store.getUser().getUserProfileUrl());
        response.setStoreUserDeposit(store.getUser().getUserDeposit());
        response.setStoreIdx(store.getStoreIdx());
        response.setGifticonDataImageName(store.getGifticon().getGifticonDataImageUrl());
        response.setGifticonName(store.getGifticon().getGifticonName());
        response.setGifticonEndDate(DateTimeConverter.getInstance().convertString(store.getGifticon().getGifticonEndDate()));
        response.setStoreEndDate(DateTimeConverter.getInstance().convertString(store.getStoreEndDate()));
        response.setDeposit(false);
        response.setStorePrice(store.getStorePrice());

        return response;
    }

    //StoreList -> StoreItemDataList 매핑
    @Named("S2S")
    default StoreItemData storeToItemData(Store store) {
        StoreItemData itemData = new StoreItemData();

        itemData.setStoreIdx(store.getStoreIdx());
        itemData.setGifticonDataImageName(store.getGifticon().getGifticonDataImageUrl());
        itemData.setGifticonName(store.getGifticon().getGifticonName());
        itemData.setGifticonEndDate(DateTimeConverter.getInstance().convertString(store.getGifticon().getGifticonEndDate()));
        itemData.setStoreEndDate(DateTimeConverter.getInstance().convertString(store.getStoreEndDate()));
        itemData.setDeposit(false);
        itemData.setStorePrice(store.getStorePrice());

        return itemData;
    }

    @IterableMapping(qualifiedByName = "S2S")
    List<StoreItemData> storesToItemDatas(List<Store> storeList);
}