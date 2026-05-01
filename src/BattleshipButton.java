import javax.swing.*;

public class BattleshipButton extends JButton
{
    private int row;
    private int col;
    private String status;

    public BattleshipButton(int row, int col)
    {
        super();
        this.row = row;
        this.col = col;
    }
    public int getRow(){return this.row;}
    public int getCol(){return this.col;}
    public String getStatus(){return this.status;}
    public void setStatus(String status){this.status = status;}
}
