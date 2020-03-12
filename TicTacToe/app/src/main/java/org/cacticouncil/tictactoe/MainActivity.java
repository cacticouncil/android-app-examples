package org.cacticouncil.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;
import java.util.Vector;


public class MainActivity extends AppCompatActivity
{
    public enum GameMode
    {
        PVP,
        PVC,
        CVC,
    }

    public enum GameDiff
    {
        HARD,
        EASY,
    }

    public enum Winner
    {
        TBD,
        X,
        O,
        DRAW
    }

    private class Move
    {
        public int score;
        public int location;
    }

    private final byte X     = (byte)'x';
    private final byte O     = (byte)'o';
    private final long DELAY = 300;

    private MainActivity.GameMode mMode;
    private MainActivity.GameDiff mDiff;
    private byte[]         mGameBoard;
    private boolean        mIsPlayerX;
    private Vector<XOView> mXOViews;
    private TextView mPlayerTurnLbl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // read startup params
        Bundle b = getIntent().getExtras();
        mMode = MainActivity.GameMode.values()[b.getInt("mode")];
        mDiff = MainActivity.GameDiff.values()[b.getInt("diff")];

        // init members
        mIsPlayerX = true;
        mPlayerTurnLbl = (TextView)findViewById(R.id.tvPlayerTurn);
        mGameBoard = new byte[9];
        mXOViews = new Vector<XOView>();
        findXOViews((ViewGroup)getWindow().getDecorView().getRootView(), mXOViews);
        for (int i=0; i<9; i++) mXOViews.get(i).setLocation(i);

        setTurnLabel();

        // first move goes to computer
        if (mMode == MainActivity.GameMode.CVC)
            new MainActivity.ComputerTurn().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    public MainActivity.GameMode getGameMode()
    {
        return mMode;
    }

    public boolean isPlayerX()
    {
        return mIsPlayerX;
    }

    public void endTurn(int loc, byte val)
    {
        mGameBoard[loc] = val;

        MainActivity.Winner win = checkWin(mIsPlayerX, mGameBoard);
        if (win != MainActivity.Winner.TBD)
        {
            try { Thread.sleep(1000); }
            catch (Exception ex) {}

            Intent i = new Intent(this, OverActivity.class);
            i.putExtra("winner", win.ordinal());
            startActivity(i);
            finish();
            return;
        }

        mIsPlayerX = !mIsPlayerX;

        setTurnLabel();
        mPlayerTurnLbl.postInvalidate();

        // next move is computer turn
        if (mMode == MainActivity.GameMode.CVC || (mMode == MainActivity.GameMode.PVC && !mIsPlayerX))
            new MainActivity.ComputerTurn().execute();
    }

    private void setTurnLabel()
    {
        mPlayerTurnLbl.setText("Player " + ((mIsPlayerX) ? "X" : "O"));
    }

    private MainActivity.Winner checkWin(boolean isX, byte[] board)
    {
        byte player = (isX) ? X : O;

        // check rows
        if ((board[0] == player) &&
                (board[1] == player) &&
                (board[2] == player) )
            return (isX) ? MainActivity.Winner.X : MainActivity.Winner.O;
        if ((board[3] == player) &&
                (board[4] == player) &&
                (board[5] == player) )
            return (isX) ? MainActivity.Winner.X : MainActivity.Winner.O;
        if ((board[6] == player) &&
                (board[7] == player) &&
                (board[8] == player) )
            return (isX) ? MainActivity.Winner.X : MainActivity.Winner.O;

        // check cols
        if ((board[0] == player) &&
                (board[3] == player) &&
                (board[6] == player) )
            return (isX) ? MainActivity.Winner.X : MainActivity.Winner.O;
        if ((board[1] == player) &&
                (board[4] == player) &&
                (board[7] == player) )
            return (isX) ? MainActivity.Winner.X : MainActivity.Winner.O;
        if ((board[2] == player) &&
                (board[5] == player) &&
                (board[8] == player) )
            return (isX) ? MainActivity.Winner.X : MainActivity.Winner.O;

        // check diagonals
        if ((board[0] == player) &&
                (board[4] == player) &&
                (board[8] == player) )
            return (isX) ? MainActivity.Winner.X : MainActivity.Winner.O;
        if ((board[2] == player) &&
                (board[4] == player) &&
                (board[6] == player) )
            return (isX) ? MainActivity.Winner.X : MainActivity.Winner.O;

        // check full board
        int count=0;
        for (int i=0; i<9; i++)
            if (board[i] != 0)
                count++;
        if (count==9) return MainActivity.Winner.DRAW;

        return MainActivity.Winner.TBD;
    }

    private MainActivity.Move minimax(boolean isX, byte[] board)
    {
        MainActivity.Move best     = new MainActivity.Move();
        best.score    = isX ? Byte.MIN_VALUE : Byte.MAX_VALUE;
        best.location = -1;

        for (int i=0; i<9; i++)
        {
            if (board[i] == 0)
            {
                // try move
                MainActivity.Move attempt = new MainActivity.Move();
                attempt.score = 0;
                attempt.location = i;
                board[i] = (isX) ? X : O;

                // check for and evaluate win condition
                MainActivity.Winner win = checkWin(isX, board);
                if (win != MainActivity.Winner.TBD)
                {
                    if (win == MainActivity.Winner.DRAW)
                        attempt.score = 0;
                    else
                    {
                        byte score = 1;
                        for (int j=0; j<9; j++)
                            if (board[j] == 0)
                                score++;

                        if (win == MainActivity.Winner.O)
                            attempt.score = -score;
                        if (win == MainActivity.Winner.X)
                            attempt.score = score;
                    }
                }
                // keep playing
                else
                {
                    attempt.score = minimax(!isX, board).score;
                }

                // remember best move
                if ((isX && (attempt.score > best.score)) || (!isX && (attempt.score < best.score)))
                {
                    best.score = attempt.score;
                    best.location = attempt.location;
                }

                // untry move
                board[i] = 0;
            }
        }

        return best;
    }

    //    public static String getViewIDName(View view, Class<?> clazz)
    //    {
    //        Integer id  = view.getId();
    //        Field[] ids = clazz.getFields();
    //
    //        for (int i = 0; i < ids.length; i++)
    //        {
    //            Object val = null;
    //
    //            try
    //            {
    //                val = ids[i].get(null);
    //            }
    //            catch (Exception ex) {}
    //
    //            if (val != null && val instanceof Integer && ((Integer) val).intValue() == id.intValue())
    //                return ids[i].getName();
    //        }
    //
    //        return "";
    //    }

    public void findXOViews(ViewGroup parent, Vector<XOView> list)
    {
        View child = null;
        int count = parent.getChildCount();
        for (int i=0; i<count; i++)
        {
            child = parent.getChildAt(i);
            if (child instanceof XOView)
                list.add((XOView) child);
            else if (child instanceof ViewGroup)
                findXOViews((ViewGroup)child, list);
        }
    }

    private class ComputerTurn extends AsyncTask<Void, Void, Void>
    {
        private MainActivity.Move mBestMove;
        private long  mStartTime;

        @Override
        protected void onPreExecute()
        {
            mStartTime = System.currentTimeMillis();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            switch (mDiff)
            {
                case EASY:
                    int location;
                    while (true)
                    {
                        location = (new Random()).nextInt(9);
                        if (mGameBoard[location] == 0)
                        {
                            mBestMove = new MainActivity.Move();
                            mBestMove.location = location;
                            break;
                        }
                    }
                    break;

                case HARD:
                    mBestMove = minimax(mIsPlayerX, mGameBoard);
                    break;

                default: break;
            }

            long elapsed = Math.abs(System.currentTimeMillis() - mStartTime);
            if (elapsed < DELAY)
            {
                try { Thread.sleep(DELAY-elapsed); }
                catch (Exception ex) {}
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            mXOViews.get(mBestMove.location).select();
        }
    }
}
