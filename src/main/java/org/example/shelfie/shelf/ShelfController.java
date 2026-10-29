package org.example.shelfie.shelf;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shelves")
public class ShelfController {

    private final ShelfService shelfService;

    public ShelfController(ShelfService shelfService) {
        this.shelfService = shelfService;
    }

    @GetMapping
    public List<ShelfDto> getMyShelves(){
        return shelfService.findMyShelves();
    }

    @GetMapping("/{id}/entries")
    public List<ShelfEntryDto> getEntries(@PathVariable Long id){
        return shelfService.findEntries(id);
    }

}
