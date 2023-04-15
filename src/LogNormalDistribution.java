import java.util.Random;

public class LogNormalDistribution { //Note that this class needs more work, will update soon

    private static int minX;
    private static int maxX;
    private static double mu;
    private static double sigma;
    static Random random = new Random();

    public LogNormalDistribution(){
        minX = 180;
        maxX = 1500;
        mu = Math.log(275);
        sigma = 0.425;
    }

    public LogNormalDistribution(int minX, int maxX, double mu, double sigma){
        this.minX = minX;
        this.maxX = maxX;
        this.mu = mu;
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
        int randomX;
        do {
            double gaussianValue = random.nextGaussian() * sigma + mu;
            randomX = (int)Math.exp(mu + sigma * gaussianValue);
        } while (randomX < minX || randomX > maxX);

        return randomX;
    }
}
