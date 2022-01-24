package com.imooc.architect.service;

import com.imooc.architect.entity.Fridge;

/**
 * Created by 姚半仙.
 */
public interface FridgeService {

    public boolean openFridge(Long id);

    public boolean closeFridge(Long id);

    public boolean putAnimal(Long fridgeId, Long animalId);

//    public Fridge lockFridge(Long id);

    public Fridge clearFridge(Long id);
}
