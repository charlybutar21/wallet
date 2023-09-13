package com.example.wallet.service;

public interface BaseService <EntityRequestClass extends Object, ResponseModelClass extends Object> {

    ResponseModelClass execute(EntityRequestClass request) throws Exception;
}
