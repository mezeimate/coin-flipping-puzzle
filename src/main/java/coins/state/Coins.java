package coins.state;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.annotation.processing.Generated;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.apache.commons.math3.util.Combinations;

/**
 * Represents the state of the coin flipping puzzle. A sequence of coins is
 * represented internally as a {@link BitSet}, in which a 0 bit represents a
 * head and an 1 bit represents a tail, respectively.
 */
public class Coins implements Cloneable {

    private final int n;
    private final int m;
    private BitSet coins;
    private final List<BitSet> flips;

    /**
     * Creates a {@code Coin} object with all coins with heads facing up.
     *
     * @param n the number of coins
     * @param m the number of coins to flip in a move
     * @throws IllegalArgumentException if {@code n} is less than 1, or {@code m}
     *         is less than 1, or {@code m} is greater than {@code n}
     */
    public Coins(int n, int m) {
        this(n, m, new BitSet(n));
    }

    /**
     * Creates a {@code Coin} object from a {@code BitSet}.
     *
     * @param n the number of coins
     * @param m the number of coins to flip in a move
     * @param coins a configuration of coins represented as a {@code BitSet}
     * @throws IllegalArgumentException if {@code n} is less than 1, or {@code m}
     *         is less than 1, or {@code m} is greater than {@code n}, or the
     *         actual length of the {@link BitSet} is greater than {@code n}
     */
    public Coins(int n, int m, BitSet coins) {
        checkArguments(n, m, coins);
        this.n = n;
        this.m = m;
        this.coins = (BitSet) coins.clone();
        flips = generateFlips(n, m);
    }
    /**
     * {@return the number of coins}
     */
    public int getN() {
        return n;
    }

    /**
     * {@return the number of coins to flip in a move}
     */
    public int getM() {
        return m;
    }

    /**
     * {@return a copy of the {@code BitSet} representing the coins}
     */
    public BitSet getCoins() {
        return (BitSet) coins.clone();
    }

    /**
     * Returns whether the puzzle is solved.
     *
     * @return {@code true} if the puzzle is solved, {@code false} otherwise
     */
    public boolean isGoal() {
        return coins.cardinality() == n;
    }

    /**
     * Returns whether the coins at the positions specified by a {@code BitSet}
     * can be flipped.
     *
     * @param which a {@code BitSet} that specifies the positions of the coins
     *              to flip
     * @return {@code true} if the coins specified can be flipped,
     *         {@code false} otherwise
     */
    public boolean canFlip(BitSet which) {
        return which.length() <= n && which.cardinality() == m;
    }

    /**
     * Flips the coins at the positions specified by a {@code BitSet}. In the
     * {@code BitSet}, 1 bits correspond to the positions of the coins to be
     * flipped.
     *
     * @param which a {@code BitSet} that specifies the positions of the coins
     *              to flip
     */
    public void flip(BitSet which) {
        coins.xor(which);
    }

    /**
     * Returns a list of all possible flips for the arguments specified. Flips
     * are represented as {@code BitSet}s.
     *
     * @param n the number of coins
     * @param m the number of coins to flip in a move
     * @return a list of all possible flips for the arguments specified
     * @throws IllegalArgumentException if {@code n} is less than 1, or {@code m}
     *         is less than 1, or {@code m} is greater than {@code n}
     */
    public static List<BitSet> generateFlips(int n, int m) {
        checkArguments(n, m);
        ArrayList<BitSet> flips = new ArrayList<>();
        for (int[] indices : new Combinations(n, m)) {
            BitSet flip = new BitSet(n);
            for (var i : indices) {
                flip.set(i);
            }
            flips.add(flip);
        }
        return flips;
    }

    /**
     * {@return a list that contains all possible flips}
     */
    public List<BitSet> getFlips() {
        return deepCopy(flips);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return (o instanceof Coins c) && n == c.n && m == c.m && coins.equals(c.coins);
    }

    @Override
    public int hashCode() {
        return Objects.hash(n, m, coins);
    }

    @Override
    public Coins clone() {
        Coins copy;
        try {
            copy = (Coins) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        copy.coins = (BitSet) coins.clone();
        return copy;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("|");
        for (var i = 0; i < n; i++) {
            sj.add((coins.get(i)) ? "1" : "O");
        }
        return sj.toString();
    }

    private static void checkArguments(int n, int m) {
        if (n < 1 || m < 1 || n < m) {
            throw new IllegalArgumentException();
        }
    }

    private static void checkArguments(int n, int m, BitSet coins) {
        checkArguments(n, m);
        if (coins.length() > n) {
            throw new IllegalArgumentException();
        }
    }

    private static List<BitSet> deepCopy(List<BitSet> list) {
        List<BitSet> copy = new ArrayList<>(list.size());
        for (var e : list) {
            copy.add((BitSet) e.clone());
        }
        return copy;
    }

    public static void main(String[] args) {
        var coins = new Coins(7, 3);
        System.out.println(coins);
        System.out.println(coins.getFlips());
    }

}
