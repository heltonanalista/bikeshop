package com.bike.shop.repository;

import com.bike.shop.model.BikeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeShopRepository extends JpaRepository<BikeOrder,Long> {
}
