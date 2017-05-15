package sample.Game.Mechanics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.Game.Mechanics.Cards.CardDeck;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Messages.SystemMessages.MessageContainer;
import sample.Lobby.Views.LobbyGameView;
import sample.Lobby.Views.UserGameView;
import sample.Game.Mechanics.Cards.GameCard;
import sample.ResourceManager.ResourceManager;
import sample.Game.Mechanics.States.*;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by ksg on 25.04.17.
 */


@SuppressWarnings("DefaultFileTemplate")
public class MainMechanics {
    public enum  ErrorCodes{
        SERVER_ERROR,
        @SuppressWarnings("unused")RESOURCE_ERROR,
        INVALID_COMMAND,
        FINISHED,
        OK,
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMechanics.class);

    public class GameContext{
        public final Map<String,GameUserItem> mp;
        public final Map<String, GameCard> table;
        public GameCard[] cards;
        public Queue<GameUserItem> masterQeue;
        public CardDeck deck;
        public GameState state;
        public int numberOfPlayers;
        public ObjectMapper mapper;
        public int numberCardsInHand;
        public int currentRound;
        public GameContext(){
            state = new InitState(this);
            mp = new HashMap<>(); // TODO: Do it as user controller
            table = new HashMap<>();
            masterQeue = new ArrayDeque<>();
            cards = new GameCard[3];
            currentRound = 0;
        }
    }
    private LobbyGameView view;
    private final GameContext context;
    public MainMechanics(ObjectMapper mapper){
        context = new GameContext();
        context.mapper = mapper;
    }
    public ErrorCodes init(LobbyGameView view, ResourceManager manager){
        LOGGER.debug("game created");
        this.view = view;
        Vector<UserGameView> vtr = view.getList();
        context.deck = manager.getCardDeck();
        if(context.deck==null){
            LOGGER.error("deck is null some trouble with resources");
            return ErrorCodes.SERVER_ERROR;
        }
        for(UserGameView cur: vtr){
            if(cur == null){
                LOGGER.error("null user error");
                return ErrorCodes.SERVER_ERROR;
               // if i'll do big lock in lobby - nevermore
            }
            String id = cur.getUserId();
            context.numberCardsInHand = view.getCardsInHand();
            Vector<GameCard> cards = context.deck.popCards(context.numberCardsInHand);//TODO: resource this
            if(cards==null){
                LOGGER.error("deck is null some trouble with resources");
                return ErrorCodes.SERVER_ERROR;
            }
            GameUserItem curPlayer = new GameUserItem(cards,context.mapper);
            context.mp.put(id, curPlayer);
            context.masterQeue.add(curPlayer);
        }
        context.numberOfPlayers = view.getNumber();
        return ErrorCodes.OK;
    }
    public ErrorCodes handleMessage(MessageContainer msg){
        GameState.ErrorCodes err = context.state.handle(msg);
        return errorHandler(err);
    }
    private ErrorCodes errorHandler(GameState.ErrorCodes err){
        switch (err){
            case OK: {
                break;
            }
            case INVALID_COMMAND: {
                return ErrorCodes.INVALID_COMMAND;
            }
            case FINISHED: {
                LOGGER.info("GameFinished \n");
                return ErrorCodes.FINISHED;
            }
        }
        return ErrorCodes.OK;
    }
    public LobbyGameView getUsersView(){
        return view;
    }
    public  void finishGame(){
        for(Map.Entry<String, GameUserItem> entry : context.mp.entrySet()){
            entry.getValue().close();
        }
    }

}
