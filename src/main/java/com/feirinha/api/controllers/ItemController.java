package com.feirinha.api.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feirinha.api.dtos.ItemDTO;
import com.feirinha.api.models.ItemModel;
import com.feirinha.api.repositories.ItemRepository;
import com.feirinha.api.services.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemRepository itemRepository;
    final ItemService itemService;

    ItemController(ItemService itemService, ItemRepository itemRepository) {
        this.itemService = itemService;
        this.itemRepository = itemRepository;
    }

    @PostMapping()
    public ResponseEntity<Object> createItem(@RequestBody @Valid ItemDTO body) {
        Optional<ItemModel> item = itemService.createItem(body);

        if (!item.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(item.get());
    }

    @GetMapping()
    public ResponseEntity<Object> getItems() {
        List<ItemModel> items = itemService.getItems();
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable("id") Long id) {
        Optional<ItemModel> item = itemService.getItemById(id);

        if (!item.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(item.get());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateItem(@PathVariable("id") Long id, @RequestBody @Valid ItemDTO body) {
        Optional<ItemModel> item = itemService.updateItem(id, body);

        if (item.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ItemModel newItem = item.get();

        if (itemRepository.existsByNameAndIdNot(body.getName(), id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(newItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable("id") Long id) {
        Optional<ItemModel> item = itemService.deleteItem(id);
        
        if (item.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } 
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
    
}
