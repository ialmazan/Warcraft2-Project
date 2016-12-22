package com.ecs160.nittacraft;

import android.os.AsyncTask;

import com.ecs160.fbs.APIRequest;
import com.ecs160.fbs.APIRequestType;
import com.ecs160.fbs.APIResponse;
import com.ecs160.fbs.Command;
import com.ecs160.fbs.CommandRequest;
import com.ecs160.fbs.CommandResponse;
import com.ecs160.fbs.CreateHostedGameRequest;
import com.ecs160.fbs.CreateHostedGameResponse;
import com.ecs160.fbs.DestroyGameRequest;
import com.ecs160.fbs.DestroyGameResponse;
import com.ecs160.fbs.EndGameRequest;
import com.ecs160.fbs.EndGameResponse;
import com.ecs160.fbs.Game;
import com.ecs160.fbs.GameConstants;
import com.ecs160.fbs.GameUpdateRequestAIToHuman;
import com.ecs160.fbs.GameUpdateRequestChangeStatus;
import com.ecs160.fbs.GameUpdateRequestTryToStartGame;
import com.ecs160.fbs.GameUpdateResponse;
import com.ecs160.fbs.GetJoinableGamesRequest;
import com.ecs160.fbs.GetJoinableGamesResponse;
import com.ecs160.fbs.JoinGameRequest;
import com.ecs160.fbs.JoinGameResponse;
import com.ecs160.fbs.LogInRequest;
import com.ecs160.fbs.LogInResponse;
import com.ecs160.fbs.LogOutRequest;
import com.ecs160.fbs.LogOutResponse;
import com.ecs160.fbs.Player;
import com.ecs160.fbs.PlayerType;
import com.ecs160.fbs.UserCredentials;
import com.google.flatbuffers.FlatBufferBuilder;

import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class CMultiplayer {
    public class PlayerInfo {
        String name;
        int color;
        int type;
        int ready;
    }

    public class GameInfo {
        String host;
        boolean startGame;
        int numPlayers;
        ArrayList<PlayerInfo> players;
        String mapName;

        public GameInfo(Game game, GameConstants gameConstants) {
            host = game.host();
            startGame = game.startGame();
            numPlayers = gameConstants.playerCount();
            mapName = gameConstants.mapName();

            players = new ArrayList<PlayerInfo>(game.playersLength());
            for (int i = 0; i < game.playersLength(); i++) {
                Player player = game.players(i);
//                players.get(i).name = player.name();
                //TODO: Figure Out PlayerColor
                PlayerInfo playerInfo = new PlayerInfo();
                playerInfo.name = player.name();
                playerInfo.color = player.color();
                playerInfo.type = player.playerType();
                playerInfo.ready = player.playerStatus();
                players.add(playerInfo);
//                players.get(i).color = player.color();
//                players.get(i).ready = player.playerStatus();
//                switch (player.playerType()) {
//                    case 0: players.get(i).type = PlayerType.EasyAI;
//                        break;
//                    case 1: players.get(i).type = PlayerType.MediumAI;
//                        break;
//                    case 2: players.get(i).type = PlayerType.HardAI;
//                        break;
//                    case 3: players.get(i).type = PlayerType.Human;
//                        break;
//                    case 4: players.get(i).type = PlayerType.Open;
//                        break;
//                    default: players.get(i).type = PlayerType.Open;
//                        break;
//                }
            }
        }

        public void Update(Game game) {
            startGame = game.startGame();
            for (int i = 0; i < game.playersLength(); i++) {
                 Player player = game.players(i);
                 players.get(i).name = player.name();
                 players.get(i).color = player.color();
                 players.get(i).ready = player.playerStatus();
                switch(player.playerType()) {
                    case 0: players.get(i).type = PlayerType.EasyAI;
                        break;
                    case 1: players.get(i).type = PlayerType.MediumAI;
                        break;
                    case 2: players.get(i).type = PlayerType.HardAI;
                        break;
                    case 3: players.get(i).type = PlayerType.Human;
                        break;
                    case 4: players.get(i).type = PlayerType.Open;
                        break;
                    default: players.get(i).type = PlayerType.Open;
                        break;
                }
            }
        }

        public String getHost() {return host;}
        public boolean getStartGame() {return startGame;}
        public int getNumPlayers() {return numPlayers;}
        ArrayList<PlayerInfo> getPlayers() {return players;}
        public String getMapName() {return mapName;}
    }

    GameInfo game;
    ZMQ.Context context;
    ZMQ.Socket chatSocket;
    ZMQ.Socket cmdSocket;
    String id;

    public CMultiplayer() {
        game = null;
        chatSocket = null;
        cmdSocket = null;
        id = "";
    }

    public GameInfo getGame() {return game;}
    public ZMQ.Socket getCmdSocket() {return cmdSocket;};

    public class SendGameResults extends AsyncTask<String, Void, Boolean> {
        public SendGameResults() {
            super();
        }
        public SendGameResults(byte winColor) {this.winColor = winColor;}
        private byte winColor;
        public boolean requestStatus;
        protected Boolean doInBackground(String... params) {
            requestStatus = false;
            String host = params[1];
            FlatBufferBuilder builder = new FlatBufferBuilder(0);
            int winners[] = new int[1];
            int numLosers = game.getNumPlayers() - 1;
            int losers[] = new int[numLosers];
            int losersItr = 0;
            for (int index = 0; index < numLosers + 1; index++)
            {
                PlayerInfo player = game.getPlayers().get(index);
                if (winColor == player.color)
                {
                    winners[0] = builder.createString(player.name);
                }

                else
                {
                    losers[losersItr] = builder.createString(player.name);
                    losersItr++;
                }
            }

            int hostOffset = builder.createString(host);
            int winnersOffset = builder.createVectorOfTables(winners);
            int losersOffset = builder.createVectorOfTables(losers);
            int endGameRequestOffset = EndGameRequest.createEndGameRequest(builder, winnersOffset, losersOffset);
            int requestOffset = APIRequest.createAPIRequest(builder, APIRequestType.EndGameRequest, endGameRequestOffset);
            builder.finish(requestOffset);

            //send request
            byte[] buf = builder.sizedByteArray();
            cmdSocket.send(buf);

            //get reply from server
            ZMsg reply = ZMsg.recvMsg(cmdSocket);
            ByteBuffer inbuf = ByteBuffer.wrap(reply.pop().getData());
            APIResponse response = APIResponse.getRootAsAPIResponse(inbuf);
            EndGameResponse endGameResponse = (EndGameResponse) response.response(new EndGameResponse());

            return com.ecs160.fbs.Status.Success == endGameResponse.status();
        }

        protected void onPostExecute(boolean status) {this.requestStatus = status;}
    }

    public class GetOnlineGames extends AsyncTask<String, Void, ArrayList<Game>> {
        public GetOnlineGames() {
            super();
        }
        public ArrayList<Game> games;

        protected ArrayList<Game> doInBackground(String... player) {
            games = new ArrayList<Game>();

            //Create GetJoinableGamesRequest
            FlatBufferBuilder builder = new FlatBufferBuilder(0);
            int playerOffset = builder.createString(player[0]);
            int getJoinableGamesRequestOffset = GetJoinableGamesRequest
                    .createGetJoinableGamesRequest(builder, playerOffset);
            int requestOffset = APIRequest.createAPIRequest(builder, APIRequestType
                    .GetJoinableGamesRequest, getJoinableGamesRequestOffset);
            builder.finish(requestOffset);

            //Get byte buffer to send over ZMQ Socket
            byte[] buf = builder.sizedByteArray();
            cmdSocket.send(buf);

            //get reply from server
            ZMsg reply = ZMsg.recvMsg(cmdSocket);

            //Read incoming data
            ByteBuffer response = ByteBuffer.wrap(reply.pop().getData());
            APIResponse apiResponse = APIResponse.getRootAsAPIResponse(response);
            GetJoinableGamesResponse getJoinableGamesResponse = (GetJoinableGamesResponse)
                    apiResponse.response(new GetJoinableGamesResponse());

            //if operation failed, return empty ArrayList
            if (com.ecs160.fbs.Status.Success != getJoinableGamesResponse.status()) {
                return new ArrayList<Game>();
            }

            //else, copy contents of flatbuffer

            ArrayList<Game> games = new ArrayList<Game>();
            for (int i = 0; i < getJoinableGamesResponse.joinableGamesLength(); i++)
            {
                games.add(getJoinableGamesResponse.joinableGames(i));
            }
            return games;
        }

        @Override
        protected void onPostExecute(ArrayList<Game> newGames) {
            games = newGames;
        }
    }

    public class SendLoginRequest extends AsyncTask<String, Void, Boolean> {
        public SendLoginRequest() {
            super();
        }
        public boolean requestStatus;
        protected Boolean doInBackground(String...params) {
            requestStatus = false;
            String username = params[0];
            String password = params[1];
            id = username;
            String temp = id + "-chat";
            context = ZMQ.context(1);
            chatSocket = context.socket(ZMQ.DEALER);
            chatSocket.setIdentity(temp.getBytes());
            chatSocket.connect("tcp://workwork.westus.cloudapp.azure.com:8080");

            temp = id + "-command";
            cmdSocket = context.socket(ZMQ.DEALER);
            cmdSocket.setIdentity(temp.getBytes());
            cmdSocket.connect("tcp://workwork.westus.cloudapp.azure.com:8080");

            // Create and serialize UserCredentials data
            FlatBufferBuilder builder = new FlatBufferBuilder(0);
            int usernameOffset = builder.createString(username);
            int passwordOffset = builder.createString(password);
            UserCredentials.startUserCredentials(builder);
            UserCredentials.addUsername(builder, usernameOffset);
            UserCredentials.addPassword(builder, passwordOffset);
            int userCredentialsOffset = UserCredentials.endUserCredentials(builder);

            LogInRequest.startLogInRequest(builder);
            LogInRequest.addCredentials(builder, userCredentialsOffset);
            int logInRequestOffset = LogInRequest.endLogInRequest(builder);

            APIRequest.startAPIRequest(builder);
            APIRequest.addRequestType(builder, APIRequestType.LogInRequest);
            APIRequest.addRequest(builder, logInRequestOffset);
            int requestOffset = APIRequest.endAPIRequest(builder);

            builder.finish(requestOffset);

            byte[] buf = builder.sizedByteArray();

            // Send byte buffer over ZMQ socket
            cmdSocket.send(buf, 0);

            //receive reply from server
            ZMsg msg = ZMsg.recvMsg(cmdSocket);
            ByteBuffer inbuf = ByteBuffer.wrap(msg.pop().getData());
            APIResponse response = APIResponse.getRootAsAPIResponse(inbuf);
            LogInResponse loginResponse = (LogInResponse) response.response(new LogInResponse());

            return com.ecs160.fbs.Status.Success == loginResponse.status();
        }

        @Override
        protected void onPostExecute(Boolean status) {
            requestStatus = status;
        }
    }


    public class SendLogOutRequest extends AsyncTask<String, Void, Boolean> {
        public SendLogOutRequest() {
            super();
        }
        public boolean requestStatus;
        protected Boolean doInBackground(String...params) {
            requestStatus = false;
            String username = params[0];

            //serialize LogOutRequest
            FlatBufferBuilder builder = new FlatBufferBuilder(0);
            int usernameOffset = builder.createString(username);
            int logOutRequestOffset = LogOutRequest.createLogOutRequest(builder, usernameOffset);
            int requestOffset = APIRequest.createAPIRequest(builder, APIRequestType
                    .LogOutRequest, logOutRequestOffset);
            builder.finish(requestOffset);

            //send byte array over ZMQ socket
            byte[] buf = builder.sizedByteArray();
            cmdSocket.send(buf);

            //get reply from server
            ZMsg reply = ZMsg.recvMsg(cmdSocket);

            //read incoming data
            ByteBuffer inbuf = ByteBuffer.wrap(reply.pop().getData());
            APIResponse apiResponse = APIResponse.getRootAsAPIResponse(inbuf);
            LogOutResponse logOutResponse = (LogOutResponse) apiResponse.response((new
                    LogOutResponse()));

            return com.ecs160.fbs.Status.Success == logOutResponse.status();
        }

        @Override
        protected void onPostExecute(Boolean status) {
            requestStatus = status;
        }
    }



    public class CreateMultiplayerGame extends AsyncTask<String, Void, Boolean> {
        public CreateMultiplayerGame() {
            super();
        }
        public boolean requestStatus;
        public GameConstants gameConstants;
        public Game game;
        protected Boolean doInBackground(String... params) {
            requestStatus = false;
            String host = params[0];
            int numPlayers = Integer.parseInt(params[1]);
            String mapName = params[2];
            System.out.println("blahdasfadsf");

            //Create CreateHostedGameRequest
            FlatBufferBuilder builder = new FlatBufferBuilder(0);
            int hostOffset = builder.createString(host);
            int mapNameoffset = builder.createString(mapName);
            int gameConstantsOffset = GameConstants.createGameConstants(builder, hostOffset, mapNameoffset, numPlayers);
            int createHostedGameRequestOffset = CreateHostedGameRequest
                    .createCreateHostedGameRequest(builder, gameConstantsOffset);
            int requestOffset = APIRequest.createAPIRequest(builder, APIRequestType
                    .CreateHostedGameRequest, createHostedGameRequestOffset);
            builder.finish(requestOffset);

            // send byte array over ZMQ socket
            byte[] buf = builder.sizedByteArray();

            cmdSocket.send(buf);

            //get reply from server
            ZMsg reply = ZMsg.recvMsg(cmdSocket);
            System.out.println("blahdasfadsf2");

            //read incoming data
            ByteBuffer inbuf = ByteBuffer.wrap(reply.pop().getData());
            APIResponse apiResponse = APIResponse.getRootAsAPIResponse(inbuf);
            CreateHostedGameResponse createHostedGameResponse = (CreateHostedGameResponse)
                    apiResponse.response(new CreateHostedGameResponse());

            boolean success = com.ecs160.fbs.Status.Success == createHostedGameResponse.status();

            if (success) {
                System.out.println("blahh");
                gameConstants = createHostedGameResponse.gameConstants();
                game = createHostedGameResponse.game();
            }

            return success;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            requestStatus = status;
        }
    }

    public class JoinMultiplayerGame extends AsyncTask<String, Void, Boolean> {
        public JoinMultiplayerGame() {
            super();
        }
        public boolean requestStatus;
        protected Boolean doInBackground(String... params) {
            requestStatus = false;
            String username = params[0];
            String host = params[1];

            //Create JoinGameRequest
            FlatBufferBuilder builder = new FlatBufferBuilder(0);
            int usernameOffset = builder.createString(username);
            int hostOffset = builder.createString(host);
            int joinGameRequestOffset = JoinGameRequest.createJoinGameRequest(builder,
                    usernameOffset, hostOffset);
            int requestOffset = APIRequest.createAPIRequest(builder, APIRequestType
                    .JoinGameRequest, joinGameRequestOffset);
            builder.finish(requestOffset);

            // send byte buffer over ZMQ socket
            byte[] buf = builder.sizedByteArray();
            cmdSocket.send(buf);

            //get reply from server
            ZMsg reply = ZMsg.recvMsg(cmdSocket);

            //read incoming data
            ByteBuffer inbuf = ByteBuffer.wrap(reply.pop().getData());
            APIResponse apiResponse = APIResponse.getRootAsAPIResponse(inbuf);
            JoinGameResponse joinGameResponse = (JoinGameResponse) apiResponse.response(new
                    JoinGameResponse());

            return com.ecs160.fbs.Status.Success == joinGameResponse.status();
        }

        @Override
        protected void onPostExecute(Boolean status) {
            requestStatus = status;
        }
    }

    public class DestroyMultiplayerGame extends AsyncTask<String, Void, Boolean> {
        public DestroyMultiplayerGame() {
            super();
        }
        public Boolean requestStatus;

        protected Boolean doInBackground(String... params) {
            requestStatus = false;
            String username = params[0];
            String host = params[1];

            FlatBufferBuilder builder = new FlatBufferBuilder(0);
            int usernameOffset = builder.createString(username);
            int hostOffset = builder.createString(host);

            int destroyGameRequestOffset = DestroyGameRequest.createDestroyGameRequest(builder, usernameOffset, hostOffset);
            int requestOffset = APIRequest.createAPIRequest(builder, APIRequestType.DestroyGameRequest, destroyGameRequestOffset);
            builder.finish(requestOffset);

            byte[] buf = builder.sizedByteArray();
            cmdSocket.send(buf);

            //get reply from server
            ZMsg reply = ZMsg.recvMsg(cmdSocket);

            //read incoming data
            ByteBuffer inbuf = ByteBuffer.wrap(reply.pop().getData());
            APIResponse apiResponse = APIResponse.getRootAsAPIResponse(inbuf);
            DestroyGameResponse destroyGameResponse = (DestroyGameResponse)
                    apiResponse.response(new DestroyGameResponse());

            return com.ecs160.fbs.Status.Success == destroyGameResponse.status();
        }

        @Override
        protected void onPostExecute(Boolean status) {this.requestStatus = status;}
    }

    public class PollForGameUpdates extends AsyncTask<Void, Void, Boolean> {
        public PollForGameUpdates() {
            super();
        }

        public Boolean requestStatus;

        protected Boolean doInBackground(Void... V) {
            requestStatus = false;
            ZMQ.PollItem server[] = {new ZMQ.PollItem(cmdSocket, ZMQ.Poller.POLLIN)};
            ZMsg reply;

            int result = ZMQ.poll(server, 1, 10);
            if (-1 == result) {
                return false;
            } else if (server[0].isReadable()) {
                reply = ZMsg.recvMsg(cmdSocket);
                ByteBuffer inbuf = ByteBuffer.wrap(reply.pop().getData());
                APIResponse apiResponse = APIResponse.getRootAsAPIResponse(inbuf);
                GameUpdateResponse gameUpdateResponse = (GameUpdateResponse) apiResponse.response(new GameUpdateResponse());
                if (null == gameUpdateResponse.game()) {
                    return false;
                } else {
                    game.Update(gameUpdateResponse.game());
                    return true;
                }

            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean status) {this.requestStatus = status;}

    }

    public class StartGameRequest extends AsyncTask<String, Void, Void> {
        public StartGameRequest() {super();}

        protected Void doInBackground(String... params) {
            String host = params[0];
            FlatBufferBuilder builder = new FlatBufferBuilder(0);
            int hostOffset = builder.createString(host);
            int startGameRequestOffset = GameUpdateRequestTryToStartGame.createGameUpdateRequestTryToStartGame(builder, hostOffset);
            int requestOffset =  APIRequest.createAPIRequest(builder, APIRequestType.GameUpdateRequestTryToStartGame, startGameRequestOffset);
            builder.finish(requestOffset);

            byte[] buf = builder.sizedByteArray();
            cmdSocket.send(buf);

            return null;
        }
    }

    public class AItoHumanRequest extends AsyncTask<String, Void, Void> {
        public AItoHumanRequest() {super();}

        protected Void doInBackground(String... params) {
            String host = params[0];
            String AIName = params[1];
            FlatBufferBuilder builder = new FlatBufferBuilder(0);
            int hostOffset = builder.createString(host);
            int AINameOffset = builder.createString(AIName);
            int aiToHumanRequestOffset = GameUpdateRequestAIToHuman.createGameUpdateRequestAIToHuman(builder, hostOffset, AINameOffset);
            int requestOffset =  APIRequest.createAPIRequest(builder, APIRequestType.GameUpdateRequestAIToHuman, aiToHumanRequestOffset);
            builder.finish(requestOffset);

            byte[] buf = builder.sizedByteArray();
            cmdSocket.send(buf);

            return null;
        }
    }

    public class StatusChangeRequest extends AsyncTask<String, Void, Void> {
        public StatusChangeRequest() {super();}
        private byte status;
        public StatusChangeRequest(byte status) {this.status = status;}

        protected Void doInBackground(String... params) {
            String host = params[0];
            String username = params[1];
            FlatBufferBuilder builder = new FlatBufferBuilder(0);
            int hostOffset = builder.createString(host);
            int usernameOffset = builder.createString(username);
            int statusChangeRequestOffset = GameUpdateRequestChangeStatus.createGameUpdateRequestChangeStatus(builder, hostOffset, usernameOffset, status);
            int requestOffset =  APIRequest.createAPIRequest(builder, APIRequestType.GameUpdateRequestChangeStatus, statusChangeRequestOffset);
            builder.finish(requestOffset);

            byte[] buf = builder.sizedByteArray();
            cmdSocket.send(buf);

            return null;
        }
    }


    public class SendGameCommand extends AsyncTask<Integer, Void, Boolean> {
        SendGameCommand(String host, int[] assetIDs, int x, int y) {
            this.host = host;
            this.assetIDs = assetIDs;
            this.x = x;
            this.y = y;
        }

        private String host;
        int x, y;
        private int[] assetIDs;
        private Boolean commandStatus;

        protected Boolean doInBackground(Integer... ints) {
            commandStatus = false;
            byte myColor = (byte) ints[0].intValue();
            byte action = (byte) ints[1].intValue();
            byte color = (byte) ints[2].intValue();
            byte type = (byte) ints[3].intValue();


            FlatBufferBuilder builder = new FlatBufferBuilder(0);
            int hostOffset = builder.createString(host);
            int assetIDOffsets = builder.createVectorOfTables(assetIDs);
            int commandOffset = Command.createCommand(builder, hostOffset, assetIDOffsets, x, y, type, color, myColor, action);
            int commandRequestOffset = CommandRequest.createCommandRequest(builder, commandOffset);
            int requestOffset = APIRequest.createAPIRequest(builder, APIRequestType.CommandRequest, commandRequestOffset);
            builder.finish(requestOffset);

            byte[] buf = builder.sizedByteArray();
            return !cmdSocket.send(buf);
        }

        @Override
        protected void onPostExecute(Boolean status) {
            this.commandStatus = status;
        }

    }

    public class ReceiveCommands extends AsyncTask<Void, Void, ArrayList<Command>> {
        ArrayList<Command> cmds;
        protected ArrayList<Command> doInBackground(Void... V) {
            ZMQ.PollItem server[] = {new ZMQ.PollItem(cmdSocket, ZMQ.Poller.POLLIN)};
            ZMsg reply = null;
            boolean rcvd = false;

            ZMQ.poll(server, 1, 15000);
            if (server[0].isReadable()) {
                reply = ZMsg.recvMsg(cmdSocket);
                rcvd = true;
            }

            if (rcvd) {
                ByteBuffer inbuf = ByteBuffer.wrap(reply.pop().getData());
                APIResponse apiResponse = APIResponse.getRootAsAPIResponse(inbuf);
                CommandResponse commandResponse = (CommandResponse) apiResponse.response(new CommandResponse());
                ArrayList<Command> recvCmds = new ArrayList<>(commandResponse.playerCommandsLength());
                for (int i = 0; i < commandResponse.playerCommandsLength(); i++) {
                    recvCmds.add(commandResponse.playerCommands(i));
                }

                return recvCmds;
            }

            return new ArrayList<>(0);
        }

        @Override
        protected void onPostExecute(ArrayList<Command> cmds) {
            this.cmds = cmds;
        }
    }
}