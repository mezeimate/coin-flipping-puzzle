package coins.state;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.BitSet;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

class CoinsTest {

    // the original initial state
    private final Coins state1 = new Coins(7, 3);

    // the goal state
    private final Coins state2;{
        BitSet bs = new BitSet(7);
        bs.set(0, 7);
        state2 = new Coins(7, 3, bs);
    }

    private ThrowingSupplier<AssertionError> assertDoesNotTh;

    final String EXPECTED_TO_STRING_ZERO = "O|O|O|O|O|O|O";

    final String EXPECTED_TO_STRING_ONE = "1|1|1|1|1|1|1";

    @BeforeEach
    public void initialize(){
        assertDoesNotTh = AssertionError::new;
    }

    @Test
    void testIsGoalFalse(){
        assertFalse(state1.isGoal());
    }

    @Test
    void testIsGoalTrue(){
        assertTrue(state2.isGoal());
    }

    @Test
    void canFlipState1Test(){
        Coins coinsState1 = state1.clone();
        BitSet bitset1 = new BitSet(coinsState1.getN() + 1);
        bitset1.set(0, coinsState1.getM() - 1);
        bitset1.set(coinsState1.getN());
        assertFalse(coinsState1.canFlip(bitset1));
    }

    @Test
    void canFlipState2Test() {
        Coins coinsState2 = state1.clone();
        BitSet bitset2 = new BitSet(coinsState2.getN());
        bitset2.set(0, coinsState2.getM() - 1);
        bitset2.set(coinsState2.getN());
        assertFalse(coinsState2.canFlip(bitset2));
    }

    @Test
    void canFlipState3Test() {
        Coins coinsState3 = state1.clone();
        BitSet bitset3 = new BitSet(coinsState3.getN());
        assertFalse(coinsState3.canFlip(bitset3));
    }

    @Test
    void canFlipState4Test() {
        Coins coinsState4 = state1.clone();
        BitSet bitset4 = new BitSet(coinsState4.getN());
        bitset4.set(0, coinsState4.getM());
        assertTrue(coinsState4.canFlip(bitset4));
    }

    @Test
    void flipTest() {
        Coins coins = state1.clone();
        BitSet bitSet = new BitSet(coins.getN());
        bitSet.set(0, coins.getN());
        coins.flip(bitSet);
        assertEquals(bitSet, coins.getCoins());
    }

    @Test
    void generateFlipsMIsLessThanOneTest() {
        assertThrows(IllegalArgumentException.class, () -> Coins.generateFlips(1, 0));
    }

    @Test
    void generateFlipsNIsLessThanOneTest() {
        assertThrows(IllegalArgumentException.class, () -> Coins.generateFlips(0, 1));
    }

    @Test
    void generateFlipsMIsGreaterThanNTest() {
        assertThrows(IllegalArgumentException.class, () -> Coins.generateFlips(1, 2));
    }

    @Test
    void getFlipsTest() {
        assertNotEquals(0, state1.getFlips().size());
    }

    @Test
    void equalsTest() {
        BitSet bs1 = new BitSet(3);
        bs1.set(0, 3);
        Coins c1 = new Coins(3, 1, bs1);
        Coins c2 = new Coins(4, 1, bs1);
        Coins c3 = new Coins(3, 2, bs1);
        Coins c4 = new Coins(4, 2, bs1);
        Coins c5 = new Coins(3, 1, new BitSet(3));
        Coins c6 = new Coins(4, 1, new BitSet(3));
        Coins c7 = new Coins(3, 2, new BitSet(3));
        Coins c8 = new Coins(4, 2, new BitSet(3));
        Coins c9 = new Coins(3, 1, bs1);
        assertFalse(c1.equals(c2));
        assertFalse(c1.equals(c3));
        assertFalse(c1.equals(c4));
        assertFalse(c1.equals(c5));
        assertFalse(c1.equals(c6));
        assertFalse(c1.equals(c7));
        assertFalse(c1.equals(c8));
        assertFalse(c1.equals(new Object()));
        assertTrue(c1.equals(c1));
        assertTrue(c1.equals(c9));
    }

    @Test
    void hashCodeEqualsTest() {
        assertEquals(state1.hashCode(), Objects.hash(state1.getN(), state1.getM(), state1.getCoins()));
    }

    @Test
    void hashCodeNotEqualsTest() {
        assertNotEquals(state1.hashCode(), state2.hashCode());
    }

    @Test
    void testAssertThrow(){
        assertDoesNotThrow(assertDoesNotTh, state1.clone().toString());
    }

    @Test
    void toStringZerosTest() {
        assertEquals(EXPECTED_TO_STRING_ZERO, state1.toString());
    }

    @Test
    void toStringOnesTest() {
        assertEquals(EXPECTED_TO_STRING_ONE, state2.toString());
    }

    @Test
    void checkArgumentsTest() {
        BitSet bitSet = new BitSet(5);
        bitSet.set(1);
        assertThrows(IllegalArgumentException.class, () -> new Coins(1, 1, bitSet));
    }
}