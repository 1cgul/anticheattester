import java.util.Random;

public class LogNormalDistribution { //Note that this class needs more work, will update soon

    int minX, maxX;
    double mu, sigma;
    private static Random random = new Random();

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
        int max = x.getMaxX();
        int min = x.getMinX();
        double sigma = x.getSigma();
        double mu = x.getMu();
        int randomX;
        do {
            double gaussianValue = random.nextGaussian() * sigma + mu;
            randomX = (int) Math.round(Math.exp(gaussianValue));
        } while (randomX < min || randomX > max);

        return randomX;
    }
}
