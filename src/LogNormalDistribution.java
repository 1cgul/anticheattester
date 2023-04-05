import java.util.Random;

public class LogNormalDistribution { //Note that this class needs more work, will update soon

    private static Random random = new Random();

    public static void main(String[] args) {
        int minX = 180;
        int maxX = 1500;
        double mu = Math.log(275);
        double sigma = 0.425;

        int randomX = generateRandomX(minX, maxX, mu, sigma);
        System.out.println("Random x value: " + randomX);
    }

    public static int generateRandomX(int minX, int maxX, double mu, double sigma) {
        int randomX;
        do {
            double gaussianValue = random.nextGaussian() * sigma + mu;
            randomX = (int) Math.round(Math.exp(gaussianValue));
        } while (randomX < minX || randomX > maxX);

        return randomX;
    }
}
