package com.crud.tasks.service;

import com.crud.tasks.client.TrelloClient;
import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.CreatedTrelloCardDto;
import com.crud.tasks.domain.Mail;
import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloCardDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrelloServiceTest {

    @InjectMocks
    private TrelloService trelloService;

    @Mock
    private TrelloClient trelloClient;

    @Mock
    private AdminConfig adminConfig;

    @Mock
    private SimpleEmailService simpleEmailService;

    @Test
    public void shouldFetchTrelloBoards() {
        //Given
        TrelloBoardDto trelloBoardDto1 = new TrelloBoardDto("test", "testId", new ArrayList<>());
        TrelloBoardDto trelloBoardDto2 = new TrelloBoardDto("test2", "testId2", new ArrayList<>());
        List<TrelloBoardDto> trelloBoard = new ArrayList<>();
        trelloBoard.add(trelloBoardDto1);
        trelloBoard.add(trelloBoardDto2);
        when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoard);

        //When
        List<TrelloBoardDto> boards = trelloService.fetchTrelloBoards();

        //Then
        Assert.assertEquals(2, boards.size());
        Assert.assertEquals("test", boards.get(0).getName());
        Assert.assertEquals("test2", boards.get(1).getName());

    }

    @Test
    public void shouldCreateCard(){
        //Given
        TrelloCardDto trelloCardDto = new TrelloCardDto(
                "Test card",
                "Test Description",
                "Test pos",
                "test_id"
        );

        CreatedTrelloCardDto createdTrelloCardDto = new CreatedTrelloCardDto(
                "1",
                "Test card",
                "http://test.com"
        );

        when(trelloService.createTrelloCard(trelloCardDto)).thenReturn(createdTrelloCardDto);
        when(trelloClient.createNewCard(trelloCardDto)).thenReturn(createdTrelloCardDto);
        when(adminConfig.getAdminMail()).thenReturn("${admin.mail}");
//        doNothing().when(simpleEmailService).send(new Mail());

        //When
        CreatedTrelloCardDto createdCard = trelloService.createTrelloCard(trelloCardDto);

        //Then
        assertEquals("Test card", createdCard.getName());
    }
}