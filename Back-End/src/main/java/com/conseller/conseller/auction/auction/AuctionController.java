package com.conseller.conseller.auction.auction;

import com.conseller.conseller.auction.auction.dto.request.AuctionListRequest;
import com.conseller.conseller.auction.auction.dto.request.ModifyAuctionRequest;
import com.conseller.conseller.auction.auction.dto.request.RegistAuctionRequest;
import com.conseller.conseller.auction.auction.dto.response.AuctionListResponse;
import com.conseller.conseller.auction.auction.dto.response.AuctionTradeResponse;
import com.conseller.conseller.auction.auction.dto.response.DetailAuctionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {
    private final AuctionService auctionService;

    // 경매 목록
    @PostMapping
    public ResponseEntity<AuctionListResponse> getAuctionList(@RequestBody AuctionListRequest request) {
        AuctionListResponse response = auctionService.getAuctionList(request);

        return ResponseEntity.ok()
                .body(response);
    }

    // 경매 글 등록
    @PostMapping("/regist")
    public ResponseEntity<Object> registAuction(@RequestBody RegistAuctionRequest request) {
        auctionService.registAuction(request);
        return ResponseEntity.ok()
                .build();
    }

    // 경매 글 상세 보기
    @GetMapping("/{auction_idx}")
    public ResponseEntity<DetailAuctionResponse> detailAuction(@PathVariable("auction_idx") Long auctionIdx) {
        DetailAuctionResponse response =  auctionService.detailAuction(auctionIdx);

        return ResponseEntity.ok()
                .body(response);
    }

    // 경매 글 수정
    @PatchMapping("/{auction_idx}")
    public ResponseEntity<Object> modifyAuction(@PathVariable("auction_idx") Long auctionIdx, @RequestBody ModifyAuctionRequest auctionRequest) {
        auctionService.modifyAuction(auctionIdx, auctionRequest);

        return ResponseEntity.ok()
                .build();
    }

    // 경매 글 삭제
    @DeleteMapping("/{auction_idx}")
    public ResponseEntity<Object> deleteAuction(@PathVariable("auction_idx") Long auctionIdx) {
        auctionService.deleteAuction(auctionIdx);

        return ResponseEntity.ok()
                .build();
    }

    // 경매 낙찰 진행
    @GetMapping("/trade/{auction_idx}")
    public ResponseEntity<AuctionTradeResponse> tradeAuction(@PathVariable("auction_idx") Long auctionIdx, @RequestParam(name = "im") Integer index ) {
        AuctionTradeResponse response = auctionService.tradeAuction(auctionIdx, index);

        return ResponseEntity.ok()
                .body(response);
    }

    // 경매 진행 취소
    @PatchMapping("/cancel/{auction_idx}")
    public ResponseEntity<Object> cancelAuction(@PathVariable("auction_idx") Long auctionIdx) {
        auctionService.cancelAuction(auctionIdx);

        return ResponseEntity.ok()
                .build();
    }

    // 입금 완료 버튼
    @PatchMapping("/complete/{auction_idx}")
    public ResponseEntity<Object> completeAuction(@PathVariable("auction_idx") Long auctionIdx) {
        auctionService.completeAuction(auctionIdx);

        return ResponseEntity.ok()
                .build();
    }

    // 거래 확정 버튼
    @PatchMapping("/confirm/{auction_idx}")
    public  ResponseEntity<Object> confirmAuction(@PathVariable("auction_idx") Long auctionIdx) {
        auctionService.confirmAuction(auctionIdx);

        return ResponseEntity.ok()
                .build();
    }


}