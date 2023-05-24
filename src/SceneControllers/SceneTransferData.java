package SceneControllers;

public class SceneTransferData {
    String filename;
    String folderName;
    String[] players;
    long [][] scores;
    double [][] accuracy;
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
    public String getFolderName() {
        return folderName;
    }
    public void setFilename(String filename){
        this.filename = filename;
    }
    public String getFilename() {
        return filename;
    }
    public void setPlayers(String[] players){
        this.players = players;
    }
    public String[] getPlayers() {
        return players;
    }
    public void setScores(long [][] scores){
        this.scores = scores;
    }
    public long[][] getScores() {
        return scores;
    }
    public void setAccuracy(double [][] accuracy){
        this.accuracy = accuracy;
    }
    public double[][] getAccuracy() {
        return accuracy;
    }
}
