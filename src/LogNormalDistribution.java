import java.util.Random;

public class LogNormalDistribution { //Note that this class needs more work, will update soon

    private static int minX;
    private static int maxX;
    private static double mu;
    private static double sigma;

    public LogNormalDistribution(){
        minX = 180;
        maxX = 2000;
        mu = Math.log(275);
        sigma = 0.6;
    }

    public LogNormalDistribution(int minX, int maxX, double mu, double sigma){
        this.minX = minX;
        this.maxX = maxX;
        this.mu = Math.log(mu);
        this.sigma = sigma;
    }
    public void setMinX(int minX){
        this.minX = minX;
    }
    public void setMaxX(int maxX){
        this.maxX = maxX;
    }
    public void setMu(double mu){
        this.mu = mu;
    }
    public void setSigma(double sigma){
        this.sigma = sigma;
    }
    public int getMaxX(){
        return maxX;
    }
    public int getMinX(){
        return minX;
    }
    public double getMu(){
        return mu;
    }
    public double getSigma(){
        return sigma;
    }
    public int generateRandomX(){
        double xValue;
        do {
            Random random = new Random();
            double normalRandomValue = random.nextGaussian();
            xValue = Math.exp(mu + sigma * normalRandomValue);
        } while (xValue < minX || xValue > maxX);
        return (int) Math.round(xValue);
    }
    public static int generateRandomX(LogNormalDistribution x) {
        double xValue;
        do {
            Random random = new Random();
            double normalRandomValue = random.nextGaussian();
            xValue = Math.exp(x.getMu() + x.getSigma() * normalRandomValue);
        } while (xValue < x.getMinX() || xValue > x.getMaxX());
        return (int) Math.round(xValue);
    }

    public static int onLoopTimer(){ //These numbers seem to work well as of now
        LogNormalDistribution x = new LogNormalDistribution(1000, 7000, 1500, .4);
        return generateRandomX(x);
    }

    public static int chopWoodTimer(){ //These numbers need testing
        LogNormalDistribution x = new LogNormalDistribution(600, 7000, 775, .35);
        return generateRandomX(x);
    }
}
