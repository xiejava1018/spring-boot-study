package com.example.demo.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.example.demo.pojo.Order;
import com.example.demo.pojo.Taco;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JdbcOrderRepository implements OrderRepository{
	private SimpleJdbcInsert orderInserter;
	private SimpleJdbcInsert orderTacoInserter;
	private ObjectMapper objectMapper;
	
	@Autowired
	public JdbcOrderRepository(JdbcTemplate jdbc)
	{
		this.orderInserter=new SimpleJdbcInsert(jdbc).withTableName("Taco_Order").usingGeneratedKeyColumns("id");
		this.orderTacoInserter=new SimpleJdbcInsert(jdbc).withTableName("Taco_Order_Tacos");
		this.objectMapper=new ObjectMapper();
	}

	@Override
	public Order save(Order order) {
		order.setPlaceAt(new Date());
		long orderId=saveOrderDetails(order);
		return order;
	}

	private long saveOrderDetails(Order order) {
		Map<String,Object> values=objectMapper.convertValue(order, Map.class);
		values.put("placedAt",order.getPlaceAt());
		long orderId=orderInserter.executeAndReturnKey(values).longValue();
		return orderId;
	}
	
	private void saveTacoToOrder(Taco taco,long orderId) {
		Map<String,Object> values =new HashMap<>();
		values.put("tacoOrder", orderId);
		values.put("taco",taco.getId());
		orderTacoInserter.execute(values);
	}

}
