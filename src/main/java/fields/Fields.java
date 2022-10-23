package fields;

import element.Diamond;
import element.Element;
import figures.Figure;
import element.Hole;

import java.util.ArrayList;

public class Fields {
    public ArrayList<Element> elements = new ArrayList<>();


    public synchronized void removeDiamond()
    {
        elements.removeIf(e -> e instanceof Diamond);
    }

    public synchronized boolean isDiamond()
    {
        for (Element e : elements
             ) {
            if(e instanceof Diamond)
                return true;
        }
        return false;
    }

    public synchronized boolean isHole()
    {
        for (Element e : elements
        ) {
            if(e instanceof Hole)
                return true;
        }
        return false;
    }

    public synchronized int indexOfHole()
    {
        for(Element e : elements)
        {
            if(e instanceof Hole)
                return elements.indexOf(e);
        }

        return -1;
    }

    public synchronized int indexOfDiamond()
    {
        for(Element e : elements)
        {
            if(e instanceof Diamond)
                return elements.indexOf(e);
        }

        return -1;
    }

    public synchronized boolean isSingleFigure()
    {
        for (Element e : elements
        ) {
            if(e instanceof Figure)
                return true;
        }
        return false;
    }

    public synchronized boolean isFigure()
    {
        int tmp=0;
        for (Element e : elements
        ) {
            if(e instanceof Figure)
                if(((Figure) e).getFigureStatus().equals(Figure.FigureStatus.PLAYING))
                    tmp++;
        }

        return tmp > 1;
    }

}
