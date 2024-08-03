package cn.rmc.mlgrush.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.ArrayList;
import java.util.Arrays;

public class ScoreBoardUtils
{
    private String[] Board;
    private int amount(){
        return Board.length;
    }

    private String[] cut(String[] content)
    {
        String[] elements = Arrays.copyOf(content, amount());

        if(elements[0] == null)
            elements[0] = "Unamed board";

        if(elements[0].length() > 32)
            elements[0] = elements[0].substring(0, 32);

        for(int i = 1; i < elements.length; i++)
            if(elements[i] != null)
                if(elements[i].length() > 40)
                    elements[i] = elements[i].substring(0, 40);

        return elements;
    }
    public void SidebarDisplay(Player p,String title, String[] args){
        ArrayList<String> result = new ArrayList<>();
        result.add(title);
        for(String s : args){
            result.add(s);
        }
        SidebarDisplay(p,result.toArray(new String[0]));
    }
    public void SidebarDisplay(Player p, String[] elements)
    {
        Board = elements;
        elements = cut(elements);
        try
        {
            if(p.getScoreboard() == null || p.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard() || p.getScoreboard().getObjectives().size() != 1)
            {
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }

            if(p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16)) == null)
            {
                p.getScoreboard().registerNewObjective(p.getUniqueId().toString().substring(0, 16), "dummy");
                p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16)).setDisplaySlot(DisplaySlot.SIDEBAR);
            }



            p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(elements[0]);

            for(int i = 1; i < elements.length; i++)
                if(elements[i] != null)
                    if(p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(elements[i]).getScore() != amount() - i)
                    {
                        p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(elements[i]).setScore(amount() - i);
                        for(String string : p.getScoreboard().getEntries())
                            if(p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16)).getScore(string).getScore() == amount() - i)
                                if(!string.equals(elements[i]))
                                    p.getScoreboard().resetScores(string);

                    }

            for(String entry : p.getScoreboard().getEntries())
            {
                boolean toErase = true;
                for(String element : elements)
                {
                    if(element != null && element.equals(entry) && p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16)).getScore(entry).getScore() == amount() - Arrays.asList(elements).indexOf(element))
                    {
                        toErase = false;
                        break;
                    }
                }

                if(toErase)
                    p.getScoreboard().resetScores(entry);

            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}