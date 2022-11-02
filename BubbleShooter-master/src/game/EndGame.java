package game;

public class EndGame {
    private Scoreboard scoreboard = new Scoreboard();


    public void finishGame(Bubble bubble) {
        if (bubble.positionY + 2 * Bubble.radius > 500){
            //scoreboard.createScoreboard("Game over");
            scoreboard.showScoreboard();
            scoreboard.addScore();
        }
        if(Bubble.bubbleList.size()==0){
            //scoreboard.createScoreboard("Victory");
            scoreboard.showScoreboard();
            scoreboard.addScore();
        }
    }
}
