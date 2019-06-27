package com.example.demo.data;

import com.example.demo.pojo.Order;

public interface OrderRepository {
	Order save(Order order);
}
