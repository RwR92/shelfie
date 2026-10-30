package org.example.shelfie.shelf;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{id}/entries")
    @ResponseStatus(HttpStatus.CREATED)
    public ShelfEntryDto addEntry(@PathVariable Long id, @Valid @RequestBody AddEntryRequest request) {
        return shelfService.addEntry(id, request);
    }

}
