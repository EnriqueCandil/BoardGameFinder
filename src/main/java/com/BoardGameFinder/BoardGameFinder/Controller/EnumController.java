package com.BoardGameFinder.BoardGameFinder.Controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BoardGameFinder.BoardGameFinder.Model.Age;
import com.BoardGameFinder.BoardGameFinder.Model.Category;
import com.BoardGameFinder.BoardGameFinder.Model.Editorial;
import com.BoardGameFinder.BoardGameFinder.Model.EstTime;
import com.BoardGameFinder.BoardGameFinder.Model.NumPlayers;

@RestController
@RequestMapping("/api/enums")
public class EnumController {

    @GetMapping("/all")
    public Map<String, List<String>> getAllEnums(){
        Map<String, List<String>> enums = new HashMap<>();
        
        enums.put("numPlayers", getEnumLabels(NumPlayers.class));  
        enums.put("estTime", getEnumLabels(EstTime.class));        
        enums.put("editorial", getEnumLabels(Editorial.class));    
        enums.put("age", getEnumLabels(Age.class));
        enums.put("categories", getEnumLabels(Category.class));

        return enums;
    }

    private <E extends Enum<E>> List<String> getEnumLabels(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(e -> {
                    if (e instanceof Category) {
                        return ((Category) e).getLabel();
                    } else if (e instanceof NumPlayers) {
                        return ((NumPlayers) e).getLabel();
                    } else if (e instanceof EstTime) {
                        return ((EstTime) e).getLabel();
                    } else if (e instanceof Editorial) {
                        return ((Editorial) e).getLabel();
                    } else if (e instanceof Age) {
                        return ((Age) e).getLabel();
                    }
                    return e.name();  // fallback, por si el enum no tiene label
                })
                .toList();
    }
}
