package Game;

import Game.Player.BlackPlayer;
import Game.Player.Player;
import Game.Player.RedPlayer;

public enum Side {
    Red() {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isRed() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public Player choosePlayer(final RedPlayer redPlayer, final BlackPlayer blackPlayer) {
            return redPlayer;
        }
    },
    Black() {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isRed() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public Player choosePlayer(final RedPlayer redPlayer, final BlackPlayer blackPlayer) {
            return blackPlayer;
        }
    };

    public abstract Player choosePlayer(final RedPlayer redPlayer, final BlackPlayer blackPlayer);

    public abstract boolean isBlack();

    public abstract boolean isRed();

    public abstract int getDirection();
}
