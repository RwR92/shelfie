package org.example.shelfie.shelf;

import org.example.shelfie.common.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShelfController.class)
class ShelfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShelfService shelfService;

    @Test
    void getMyShelvesReturnsTheShelvesAsJson() throws Exception {
        when(shelfService.findMyShelves())
                .thenReturn(List.of(new ShelfDto(3L, "Read", ShelfType.READ, 2)));

        mockMvc.perform(get("/api/shelves"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Read"))
                .andExpect(jsonPath("$[0].type").value("READ"))
                .andExpect(jsonPath("$[0].entryCount").value(2));
    }

    @Test
    void unknownShelfGivesProblemDetail404() throws Exception {
        when(shelfService.findEntries(999L))
                .thenThrow(new NotFoundException("Shelf 999 not found"));

        mockMvc.perform(get("/api/shelves/999/entries"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Shelf 999 not found"));
    }

    @Test
    void addingAnEntryReturns201() throws Exception {
        when(shelfService.addEntry(eq(3L), any(AddEntryRequest.class)))
                .thenReturn(new ShelfEntryDto(7L, 4L, "1984", "George Orwell", LocalDateTime.now(), null));

        mockMvc.perform(post("/api/shelves/3/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"bookId\": 4 }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.title").value("1984"));
    }

    @Test
    void missingBookIdIsRejectedBeforeTheServiceIsCalled() throws Exception {
        mockMvc.perform(post("/api/shelves/3/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.bookId").exists());

        verify(shelfService, never()).addEntry(any(), any());
    }
}