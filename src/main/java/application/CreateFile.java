package application;

import figures.Figure;
import figures.RegularFigure;
import figures.SuperFastFigure;
import player.Player;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static application.MenuController.handler;

public class CreateFile {

    Player[] players;
    String finalTime;

    {
        // ime logger-a je naziv klase

        Logger.getLogger(CreateFile.class.getName()).addHandler(handler);
    }


    public CreateFile(Player[] players, String finalTime)
    {
        this.players=players;
        this.finalTime=finalTime;
        createFile();
    }

    private void createFile() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        String path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"diamondcircleresults"+File.separator+ "IGRA"+timeStamp+".txt";
        File myFile = new File(path);

        try {
            Logger.getLogger(MenuController.class.getName()).addHandler(handler);
            Logger.getLogger(MenuController.class.getName()).setUseParentHandlers(false);
            FileWriter myWriter = null;

            if(myFile.createNewFile())
                myWriter = new FileWriter(path);


            for (int i=0 ; i<players.length ; i++)
            {
                myWriter.write("Igrac - " + players[i].getName() + "\n");

                for (int j=0 ; j<players[i].figures.length ; j++) {
                    myWriter.write("\t" + "Figura "+ (j+1) +" ("+ getType(players[i].figures[j])+", "+getColorString(players[i].figures[j].getColor().toString())+
                            ") - predjeni put "+getPath(players[i].figures[j])+" - stigla do cilja "+getStatus(players[i].figures[j])+getTime(players[i].figures[j].ellapsedTime)+ "\n");
                }
            }

            myWriter.write("Total "+finalTime);

            myWriter.close();
        }
        catch (Exception e)
        {
            Logger.getLogger(CreateFile.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
        }
    }
    private String getColorString(String s)
    {
        if("0xffff00ff".equals(s))
        {
            return "Zuta";
        }
        else if("0x008000ff".equals(s))
        {
            return "Zelena";
        }
        else if("0x0000ffff".equals(s))
        {
            return "Plava";
        }
        else
            return "Crvena";
    }



    private String getType(Figure figure)
    {
        if(figure instanceof SuperFastFigure)
            return "SuperFastFigure";
        else if(figure instanceof RegularFigure)
            return "RegularFigure";
        else
            return "FlyingFigure";
    }

    private String getPath(Figure figure)
    {
        String s = "";

        for(int i = 0 ; i<figure.visitedFieldsX.size() ; i++)
        {
            s+=" ("+ figure.visitedFieldsX.get(i) +", " + figure.visitedFieldsY.get(i) +") ";
        }
        return s;
    }

    private String getTime(long millis)
    {
        long minutes
                = TimeUnit.MILLISECONDS.toMinutes(millis);

        // This method uses this formula seconds =
        // (milliseconds / 1000);
        long seconds
                = (TimeUnit.MILLISECONDS.toSeconds(millis)
                % 60);

        return " vrijeme provedeno igrajuci: "+minutes +":"+ seconds;
    }



    private String getStatus(Figure figure)
    {
        if (Figure.FigureStatus.FINISHED.equals(figure.getFigureStatus()))
            return "da";
        else
            return "ne";
    }
}
