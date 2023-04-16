import java.util.Random;

public class LogNormalDistribution { //Note that this class needs more work, will update soon

    private int minX;
    private int maxX;
    private double mu;
    private double sigma;

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
    public static int generateRandomX(LogNormalDistribution x) {
        int max = x.getMaxX();
        int min = x.getMinX();
        double sigma = x.getSigma();
        double mu = x.getMu();
        double xValue;
        do {
            Random random = new Random();
            double normalRandomValue = random.nextGaussian();
            xValue = Math.exp(mu + sigma * normalRandomValue);
        } while (xValue < min || xValue > max);
        return (int) Math.round(xValue);
    }
}
