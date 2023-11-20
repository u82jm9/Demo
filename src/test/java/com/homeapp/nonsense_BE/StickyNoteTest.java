package com.homeapp.nonsense_BE;

import com.homeapp.nonsense_BE.models.note.StickyNote;
import com.homeapp.nonsense_BE.services.StickyNoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class StickyNoteTest {

    @Autowired
    private StickyNoteService stickyNoteService;

    @BeforeEach
    private void setup() {
        Map<String, Boolean> map1 = new HashMap<>();
        map1.put("This is the message for the before all method", false);
        StickyNote note = new StickyNote("Before All Method", map1, false);
        stickyNoteService.create(note);
        Map<String, Boolean> map2 = new HashMap<>();
        map2.put("This is the message for the second before all method", true);
        StickyNote note2 = new StickyNote("Second Before All Method", map2, true);
        stickyNoteService.create(note2);
        Map<String, Boolean> map3 = new HashMap<>();
        map3.put("This is the message for the third before all method", false);
        StickyNote note3 = new StickyNote("Third Before All Method", map3, false);
        stickyNoteService.create(note3);
    }

    @Test
    public void test_That_Retrieve_All_Returns_List_Of_Notes() {
        List<StickyNote> stickyNoteList = stickyNoteService.retrieveAllNotes();
        assertNotNull(stickyNoteList);
    }

    @Test
    public void test_That_A_StickyNote_Can_be_Created() {
        int notesBefore = stickyNoteService.retrieveAllNotes().size();
        Map<String, Boolean> map = new HashMap<>();
        map.put("First test", true);
        StickyNote note = new StickyNote("Title for test", map, true);
        stickyNoteService.create(note);
        assertTrue(notesBefore < stickyNoteService.retrieveAllNotes().size());
    }

    @Test
    public void test_That_A_StickyNote_Can_Be_Retrieved_By_Id() {
        assertNotNull(stickyNoteService.retrieveById(2L));
    }

    @Test
    public void test_That_A_StickNote_Can_Be_Edited() {
        StickyNote noteBefore = stickyNoteService.retrieveById(2L).get();
        Map<String, Boolean> map = new HashMap<>();
        map.put("Test Message", false);
        noteBefore.setMessageMap(map);
        stickyNoteService.editStickyNote(noteBefore);
        StickyNote noteAfter = stickyNoteService.retrieveById(2L).get();
        assertNotEquals(noteAfter, noteBefore);
    }

    @Test
    public void test_That_A_StickyNote_Can_Be_Retrieved_By_Title() {
        Map<String, Boolean> map = new HashMap<>();
        map.put("Message", false);
        StickyNote testNote = new StickyNote("Before All Method", map, false);
        assertNotNull(stickyNoteService.retrieveByTitle(testNote.getTitle()));
    }

    @Test
    public void test_That_Two_Notes_Cannot_Have_The_Same_Title() {
        Map<String, Boolean> map1 = new HashMap<>();
        map1.put("Test Message", false);
        StickyNote note1 = new StickyNote("Test Note", map1, false);
        stickyNoteService.create(note1);
        Map<String, Boolean> map2 = new HashMap<>();
        map1.put("Different Message", false);
        int notesBefore = stickyNoteService.retrieveAllNotes().size();
        StickyNote note2 = new StickyNote("Test Note", map2, false);
        stickyNoteService.create(note2);
        int notesAfter = stickyNoteService.retrieveAllNotes().size();
        assertEquals(notesAfter, notesBefore);
    }

    @Test
    public void test_That_A_StickyNote_Can_Be_Deleted() {
        int notesBefore = stickyNoteService.retrieveAllNotes().size();
        StickyNote note = stickyNoteService.retrieveById(2L).get();
        stickyNoteService.deleteNote(note);
        int notesAfter = stickyNoteService.retrieveAllNotes().size();
        assertTrue(notesAfter < notesBefore);
    }

    @Test
    public void test_All_StickyNotes_Can_Be_Deleted() {
        stickyNoteService.create("Delete me!", "I will be deleted, goodbye. But I will have served a purpose. Please remember me.", true);
        stickyNoteService.deleteAll();
        assertNotNull(stickyNoteService.retrieveAllNotes());
    }
}