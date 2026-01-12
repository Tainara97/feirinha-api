package com.feirinha.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.feirinha.api.dtos.ItemDTO;
import com.feirinha.api.models.ItemModel;
import com.feirinha.api.repositories.ItemRepository;

@Service
public class ItemService {
    final ItemRepository itemRepository;

    ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Optional<ItemModel> createItem(ItemDTO body) {

        if (itemRepository.existsByName(body.getName())) {
            return Optional.empty();
        }

        ItemModel item = new ItemModel(body);
        itemRepository.save(item);
        return Optional.of(item);
    }

    public List<ItemModel> getItems() {
        return itemRepository.findAll();
    }

    public Optional<ItemModel> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public Optional<ItemModel> updateItem(Long id, ItemDTO body) {
        Optional<ItemModel> item = itemRepository.findById(id);

        if (item.isEmpty()) {
            return Optional.empty(); 
        }

        ItemModel newItem = item.get();

        newItem.setName(body.getName());
        newItem.setQuantity(body.getQuantity());
        return Optional.of(itemRepository.save(newItem)); 
    }

    public Optional<ItemModel> deleteItem(Long id) {
        Optional<ItemModel> item = itemRepository.findById(id);

        if(item.isEmpty()) {
            return Optional.empty();
        }

        itemRepository.delete(item.get());
        return Optional.of(item.get());
    }
}
