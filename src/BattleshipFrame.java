import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class BattleshipFrame extends JFrame
{
    static final int FIVE_LONG = 5;
    static final int FOUR_LONG = 4;
    static final int THREE_LONG = 3;
    static final int TWO_LONG = 2;

    JButton quitBtn, playAgainBtn;
    JPanel mainPnl, statusPnl, gamePnl, footerPnl;
    JLabel missLbl, strikeLbl, missTotalLbl, hitTotalLbl;
    JTextField missTf, strikeTf, missTotalTf, hitTotalTf;
    BattleshipButton[][] gameBoard = new BattleshipButton[10][10];
    int miss = 0, totalMiss = 0, strike = 0, totalHit = 0;

    public BattleshipFrame() {
        createMainPanel();
        createStatusPanel();
        createGamePanel();
        createFooterPanel();
        populateBattleships(FIVE_LONG);
        populateBattleships(FOUR_LONG);
        populateBattleships(THREE_LONG);
        populateBattleships(THREE_LONG);
        populateBattleships(TWO_LONG);
        frame();
    }
    public void frame()
    {
        final boolean[] isValid = {false};
        for(int row = 0; row < 10; row++)
            for(int col = 0; col < 10; col++)
            {
                gameBoard[row][col].addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        isValid[0] = false;
                        BattleshipButton btn = ((BattleshipButton)e.getSource());
                        int row = btn.getRow();
                        int col = btn.getCol();
                        if(gameBoard[row][col].getText().equals("X") || gameBoard[row][col].getText().equals("M"))
                        {
                            JOptionPane.showMessageDialog(null, "Please select an empty space", "Invalid Selection", JOptionPane.ERROR_MESSAGE);
                            isValid[0] = true;
                        }
                        else if(isEmpty(row, col))
                        {
                            setArrayMISS(row, col);
                            if(strike == 3){
                                int result = JOptionPane.showConfirmDialog(null, "You lost! Would you like to play again?", "LOSER", JOptionPane.YES_NO_OPTION);
                                if(result == JOptionPane.NO_OPTION){
                                    System.exit(0);
                                }
                                else if(result == JOptionPane.YES_OPTION){
                                    resetBoard();
                                }
                            }
                            isValid[0] = true;

                        }
                        else if(!isEmpty(row, col) && gameBoard[row][col].getStatus().equals("X"))
                        {
                            setArrayHIT(row, col);
                            if(totalHit == 17)
                            {
                                int result = JOptionPane.showConfirmDialog(null, "You won! Would you like to play again?", "WINNER", JOptionPane.YES_NO_OPTION);
                                if(result == JOptionPane.NO_OPTION){
                                    System.exit(0);
                                }
                                else if(result == JOptionPane.YES_OPTION){
                                    resetBoard();
                                }
                            }
                            isValid[0] = true;
                        }
                    }
                });
            }
    }
    public void populateArrayWAVE()
    {
        for(int row = 0; row < 10; row++)
            for(int col = 0; col < 10; col++)
            {
                gameBoard[row][col] = new BattleshipButton(row, col);
                gameBoard[row][col].setText("~");
                gameBoard[row][col].setFont(new Font("Serif", Font.BOLD, 40));
                gameBoard[row][col].setForeground(Color.CYAN);
                gameBoard[row][col].setBackground(Color.WHITE);
                gameBoard[row][col].setStatus("~");
            }
    }
    public void setArrayMISS(int row, int col)
    {
        gameBoard[row][col].setText("M");
        gameBoard[row][col].setFont(new Font("Serif", Font.BOLD, 40));
        gameBoard[row][col].setForeground(Color.YELLOW);
        gameBoard[row][col].setBackground(Color.LIGHT_GRAY);
        gameBoard[row][col].setStatus("M");
        miss++; totalMiss++;
        if(miss == 5){
            miss = 0;
            strike++;
            strikeTf.setText(String.valueOf(strike));
        }
        missTf.setText(String.valueOf(miss));
        missTotalTf.setText(String.valueOf(totalMiss));
    }
    public void setArrayHIT(int row, int col)
    {
        gameBoard[row][col].setText("X");
        gameBoard[row][col].setFont(new Font("Serif", Font.BOLD, 40));
        gameBoard[row][col].setForeground(Color.RED);
        gameBoard[row][col].setBackground(Color.PINK);
        gameBoard[row][col].setStatus("X");
        totalHit++; miss = 0;

        missTf.setText(String.valueOf(miss));
        hitTotalTf.setText(String.valueOf(totalHit));
    }
    public void populateBattleships(int shipLength)
    {
        boolean isDone = false;
        while(!isDone){
            Random rand = new Random();
            boolean isValidRow = true,  isValidCol = true;
            int topBound = 0, bottomBound = 0;
            int rowRand = rand.nextInt(shipLength + 1);
            int colRand = rand.nextInt(shipLength + 1);
            //0 for vertical, 1 for horizontal
            int orientation = rand.nextInt(2);
            if(orientation == 0) {
                //checking vertically
                if(rowRand == shipLength){
                    for(int i = 10 - shipLength; i < 10; i++){
                        if (gameBoard[i][colRand].getStatus().equals("X")) {
                            isValidRow = false;
                            break;
                        }
                    }
                    bottomBound = 10 - shipLength;
                    topBound = 9;
                }
                else
                {
                    for(int i = rowRand; i < rowRand + 5; i++)
                    {
                        if (gameBoard[i][colRand].getStatus().equals("X")) {
                            isValidRow = false;
                            break;
                        }
                    }
                    bottomBound = rowRand;
                    topBound = rowRand + shipLength - 1;
                }
                if(isValidRow){
                    isDone = true;
                    for(int row = bottomBound; row < topBound + 1; row++)
                        gameBoard[row][colRand].setStatus("X");

                }
            }
            //checking horizontally
            else {
                if(colRand == shipLength){
                    for(int i = 10 - shipLength; i < 10; i++){
                        if (gameBoard[rowRand][i].getStatus().equals("X")) {
                            isValidCol = false;
                            break;
                        }
                    }
                    bottomBound = 10 - shipLength;
                    topBound = 9;
                }
                else
                {
                    for(int i = colRand; i < colRand + shipLength; i++)
                    {
                        if (gameBoard[rowRand][i].getStatus().equals("X")) {
                            isValidCol = false;
                            break;
                        }
                    }
                    bottomBound = colRand;
                    topBound = colRand + shipLength - 1;
                }
                if(isValidCol){
                    isDone = true;
                    for(int col = bottomBound; col < topBound + 1; col++)
                        gameBoard[rowRand][col].setStatus("X");

                }
            }
        }
    }
    public void createMainPanel()
    {
        setTitle("Battleship");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());
        add(mainPnl);
        populateArrayWAVE();
    }
    public void createStatusPanel()
    {
        //Status Panel (Grid Layout 2/4)
        statusPnl = new JPanel();
        statusPnl.setLayout(new GridLayout(2, 4));
        //statusPnl.setBackground(Color.CYAN);
        statusPnl.setBorder(BorderFactory.createTitledBorder("Status Panel"));
        //Miss Label
        missLbl = new JLabel("Misses");
        missLbl.setFont(new Font("Serif", Font.BOLD, 15));
        missLbl.setHorizontalAlignment(JLabel.CENTER);
        statusPnl.add(missLbl);
        //Total Misses Label
        missTotalLbl = new JLabel("Total Misses");
        missTotalLbl.setFont(new Font("Serif", Font.BOLD, 15));
        missTotalLbl.setHorizontalAlignment(JLabel.CENTER);
        statusPnl.add(missTotalLbl);
        //Strike Label
        strikeLbl = new JLabel("Strikes");
        strikeLbl.setFont(new Font("Serif", Font.BOLD, 15));
        strikeLbl.setHorizontalAlignment(JLabel.CENTER);
        statusPnl.add(strikeLbl);
        //Total Strikes Label
        hitTotalLbl = new JLabel("Total Hits");
        hitTotalLbl.setFont(new Font("Serif", Font.BOLD, 15));
        hitTotalLbl.setHorizontalAlignment(JLabel.CENTER);
        statusPnl.add(hitTotalLbl);
        //Miss Count
        missTf = new JTextField();
        missTf.setFont(new Font("Serif", Font.PLAIN, 15));
        missTf.setHorizontalAlignment(JTextField.CENTER);
        missTf.setEditable(false);
        missTf.setBackground(Color.LIGHT_GRAY);
        missTf.setText(String.valueOf(miss));
        statusPnl.add(missTf);
        //Total Misses Count
        missTotalTf = new JTextField();
        missTotalTf.setFont(new Font("Serif", Font.PLAIN, 15));
        missTotalTf.setHorizontalAlignment(JTextField.CENTER);
        missTotalTf.setEditable(false);
        missTotalTf.setBackground(Color.LIGHT_GRAY);
        statusPnl.add(missTotalTf);
        missTotalTf.setText(String.valueOf(totalMiss));
        //Strike Count
        strikeTf = new JTextField();
        strikeTf.setFont(new Font("Serif", Font.PLAIN, 15));
        strikeTf.setHorizontalAlignment(JTextField.CENTER);
        strikeTf.setEditable(false);
        strikeTf.setBackground(Color.LIGHT_GRAY);
        strikeTf.setText(String.valueOf(strike));
        statusPnl.add(strikeTf);
        //Total Strikes Count
        hitTotalTf = new JTextField();
        hitTotalTf.setFont(new Font("Serif", Font.PLAIN, 15));
        hitTotalTf.setHorizontalAlignment(JTextField.CENTER);
        hitTotalTf.setEditable(false);
        hitTotalTf.setBackground(Color.LIGHT_GRAY);
        hitTotalTf.setText(String.valueOf(totalMiss));
        statusPnl.add(hitTotalTf);

        mainPnl.add(statusPnl, BorderLayout.NORTH);
    }
    public void createGamePanel()
    {
        gamePnl = new JPanel();
        gamePnl.setLayout(new GridLayout(10, 10));
        gamePnl.setBorder(BorderFactory.createTitledBorder("Game Panel"));

        for(int row = 0; row < 10; row++)
            for(int col = 0; col < 10; col++)
            {
                gamePnl.add(gameBoard[row][col]);
            }
        mainPnl.add(gamePnl, BorderLayout.CENTER);
    }
    public void createFooterPanel()
    {
        footerPnl = new JPanel();
        footerPnl.setLayout(new GridLayout(1, 2));
        quitBtn = new JButton("Quit");
        playAgainBtn = new JButton("Play Again");
        quitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit? Current progress will be lost.", "Quit", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });
        playAgainBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to play again? Current progress will be lost.", "Play Again", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    resetBoard();
                }
            }
        });
        footerPnl.add(quitBtn);
        footerPnl.add(playAgainBtn);
        mainPnl.add(footerPnl, BorderLayout.SOUTH);
    }
    public void resetBoard()
    {
        miss = 0; totalMiss = 0; strike = 0; totalHit = 0;
        missTf.setText(String.valueOf(miss));
        missTotalTf.setText(String.valueOf(totalMiss));
        strikeTf.setText(String.valueOf(strike));
        hitTotalTf.setText(String.valueOf(totalMiss));
        for(int row = 0; row < 10; row++)
            for(int col = 0; col < 10; col++)
            {
                gameBoard[row][col].setText("~");
                gameBoard[row][col].setFont(new Font("Serif", Font.BOLD, 40));
                gameBoard[row][col].setForeground(Color.CYAN);
                gameBoard[row][col].setBackground(Color.WHITE);
                gameBoard[row][col].setStatus("~");
            }
        populateBattleships(FIVE_LONG);
        populateBattleships(FOUR_LONG);
        populateBattleships(THREE_LONG);
        populateBattleships(THREE_LONG);
        populateBattleships(TWO_LONG);
    }

    public boolean isEmpty(int row, int col)
    {
        if(gameBoard[row][col].getStatus().equals("X"))
            return false;
        else return gameBoard[row][col].getStatus().equals("~");
    }
}
