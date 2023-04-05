import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

@ScriptManifest(author = "? & reluct", info = "Chops and Banks Oak Logs at Draynor", logo = "", name = "OaknBankRE", version = 1.0D)
public class OaknBank extends Script {
    LinkedList<MousePathPoint> mousePath = new LinkedList<>();
    private int mX, mY;
    private long angle;
    private BasicStroke cursorStroke = new BasicStroke(2);
    private Color cursorColor = Color.WHITE;
    private AffineTransform oldTransform;
    private Area oakArea = new Area(3085, 3305, 3077, 3291);

    private long timeBegan;

    private long timeRan;

    private int beginningXp;

    private int currentXp;

    private int xpGained;

    private int currentLevel;

    private int beginningLevel;

    private int levelsGained;

    private int oakLogsCount;
    LogNormalDistribution x = new LogNormalDistribution();

    public void onMessage(Message m) {
        if (m.getMessage().contains("You get some oak logs."))
            this.oakLogsCount++;
    }

    public void onStart() {
        this.timeBegan = System.currentTimeMillis();
        this.beginningXp = this.skills.getExperience(Skill.WOODCUTTING);
        this.beginningLevel = this.skills.getStatic(Skill.WOODCUTTING);
    }

    public void onPaint(Graphics2D g) { //will fix this mess later


        g.setColor(Color.GREEN);
        this.timeRan = System.currentTimeMillis() - this.timeBegan;
        g.drawString(ft(this.timeRan), 12, 235);
        this.currentXp = this.skills.getExperience(Skill.WOODCUTTING);
        this.xpGained = this.currentXp - this.beginningXp;
        g.drawString("Exp Gained: " + this.xpGained, 12, 250);
        this.currentLevel = this.skills.getStatic(Skill.WOODCUTTING);
        this.levelsGained = this.currentLevel - this.beginningLevel;
        g.drawString("Start Level: " + this.beginningLevel, 12, 265);
        g.drawString("Current Level: " + this.currentLevel, 12, 280);
        g.drawString("Levels Gained: " + this.levelsGained, 12, 295);
        g.drawString("Logs obtained: " + this.oakLogsCount, 12, 310);
        oldTransform = g.getTransform();
        mX = mouse.getPosition().x;
        mY = mouse.getPosition().y;

        g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

//MOUSE TRAIL
        while (!mousePath.isEmpty() && mousePath.peek().isUp())
            mousePath.remove();
        Point clientCursor = mouse.getPosition();
        MousePathPoint mpp = new MousePathPoint(clientCursor.x, clientCursor.y, 300);
        if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp))
            mousePath.add(mpp);
        MousePathPoint lastPoint = null;
        for (MousePathPoint a : mousePath) {
            if (lastPoint != null) {
                g.setColor(new Color(255, 0, 0, a.getAlpha()));    //trail color
                g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);
            }
            lastPoint = a;
        }

        if (mX != -1) {
            g.setStroke(cursorStroke);
            g.setColor(cursorColor);
            g.drawLine(mX-3, mY-3, mX+2, mY+2);
            g.drawLine(mX-3, mY+2, mX+2, mY-3);

            g.rotate(Math.toRadians(angle+=6), mX, mY);

            g.draw(new Arc2D.Double(mX-12, mY-12, 24, 24, 330, 60, Arc2D.OPEN));
            g.draw(new Arc2D.Double(mX-12, mY-12, 24, 24, 151, 60, Arc2D.OPEN));

            g.setTransform(oldTransform);
        }
    }

    private String ft(long duration) { //for the paint clock
        String res = "";
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                .toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                .toMinutes(duration));
        if (days == 0L) {
            res = hours + ":" + minutes + ":" + seconds;
        } else {
            res = days + ":" + hours + ":" + minutes + ":" + seconds;
        }
        return res;
    }

    public int onLoop() throws InterruptedException {
        if (canCut()) {
            chopOaks();
        } else {
            bankLogs();
            underAttack();
        }
        int tempName = LogNormalDistribution.generateRandomX(x);
        log(tempName);
        return tempName;
    }

    private void chopOaks() throws InterruptedException {
        RS2Object oakTree = (RS2Object)getObjects().closest(this.oakArea, new String[] { "Oak" });
        if (readyToCut() && oakTree != null && oakTree.interact(new String[] { "Chop down" })) {
            log("Chopping Oak!");
            sleep(random(750, 800));
            getMouse().moveOutsideScreen();
            (new ConditionalSleep(5500) {
                public boolean condition() {
                    return OaknBank.this.myPlayer().isAnimating();
                }
            }).sleep();
        } else if (!this.oakArea.contains((Entity)myPlayer())) {
            log("Walking back to Oaks!");
            getWalking().webWalk(new Area[] { this.oakArea });
        }
    }

    private void bankLogs() throws InterruptedException {
        if (getInventory().isFull() && !myPlayer().isAnimating() && !Banks.DRAYNOR.contains((Entity)myPlayer())) {
            log("Walking to Bank!");
            getWalking().webWalk(new Area[] { Banks.DRAYNOR });
            (new ConditionalSleep(2500) {
                public boolean condition() {
                    return Banks.DRAYNOR.contains((Entity)OaknBank.this.myPlayer());
                }
            }).sleep();
        } else if (Banks.DRAYNOR.contains((Entity)myPlayer()) && getInventory().isFull() && !myPlayer().isUnderAttack() && !getBank().isOpen()) {
            log("Banking Logs!");
            getBank().open();
        } else if (getInventory().contains(new String[] { "Oak logs" })) {
            getBank().depositAll(new String[] { "Oak logs" });
        } else if (!getInventory().contains(new String[] { "Oak logs" })) {
            getBank().close();
        }
    }

    private void underAttack() {
        if (myPlayer().isUnderAttack()) {
            log("Under attack! Running!");
            getWalking().webWalk(new Area[] { this.oakArea });
        }
    }

    private boolean canCut() {
        return (!getInventory().isFull() && !getBank().isOpen());
    }

    private boolean readyToCut() {
        return (!myPlayer().isAnimating() && !myPlayer().isUnderAttack() && this.oakArea.contains((Entity)myPlayer()));
    }
}
