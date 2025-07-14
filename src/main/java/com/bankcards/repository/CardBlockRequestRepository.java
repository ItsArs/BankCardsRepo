package com.bankcards.repository;


import com.bankcards.entity.CardBlockRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CardBlockRequestRepository extends JpaRepository<CardBlockRequest, Long> {
    Page<CardBlockRequest> findAllByStatusEquals(CardBlockRequest.BlockRequestStatus status, Pageable pageable);
}
