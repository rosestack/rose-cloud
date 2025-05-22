/*
 * Copyright © 2025 rosestack.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.rose.core.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.EnumSet;

/**
 * Utilities method to Enums.
 *
 * @author Pedro Ruivo
 * @since infinispan 8.2
 */
public abstract class EnumUtil {

    // Represents an empty bit set.
    public static final long EMPTY_BIT_SET = 0L;

    /**
     * Converts a collection of enums into a bit set.
     * If the collection is null or empty, returns an empty bit set.
     *
     * @param enums the collection of enums
     * @param <E>   the type of enum
     * @return the bit set representing the collection of enums
     */
    public static <E extends Enum<E>> long bitSetOf(Collection<E> enums) {
        if (enums == null || enums.isEmpty()) {
            return EMPTY_BIT_SET;
        }
        long flagBitSet = EMPTY_BIT_SET;
        for (Enum<?> f : enums) {
            flagBitSet |= bitSetOf(f);
        }
        return flagBitSet;
    }

    /**
     * Converts a single enum into a bit set.
     *
     * @param first the enum
     * @return the bit set representing the enum
     */
    public static long bitSetOf(Enum<?> first) {
        return 1L << first.ordinal();
    }

    /**
     * Converts two enums into a bit set.
     *
     * @param first  the first enum
     * @param second the second enum
     * @return the bit set representing the two enums
     */
    public static long bitSetOf(Enum<?> first, Enum<?> second) {
        return bitSetOf(first) | bitSetOf(second);
    }

    /**
     * Converts an enum array into a bit set.
     *
     * @param first     the first enum
     * @param second    the second enum
     * @param remaining the remaining enums
     * @return the bit set representing the enums
     */
    public static long bitSetOf(Enum<?> first, Enum<?> second, Enum<?>... remaining) {
        long bitSet = bitSetOf(first, second);
        for (Enum<?> f : remaining) {
            bitSet |= bitSetOf(f);
        }
        return bitSet;
    }

    /**
     * Converts an enum array into a bit set.
     *
     * @param flags the enum array
     * @return the bit set representing the enum array
     */
    public static long bitSetOf(Enum<?>[] flags) {
        long bitSet = EMPTY_BIT_SET;
        for (Enum<?> flag : flags) {
            bitSet |= bitSetOf(flag);
        }
        return bitSet;
    }

    /**
     * Converts a bit set into an EnumSet.
     *
     * @param bitSet the bit set
     * @param eClass the class of the enum
     * @param <E>    the type of enum
     * @return the EnumSet representing the bit set
     */
    public static <E extends Enum<E>> EnumSet<E> enumSetOf(long bitSet, Class<E> eClass) {
        if (bitSet == EMPTY_BIT_SET) {
            return EnumSet.noneOf(eClass);
        }
        EnumSet<E> flagSet = EnumSet.noneOf(eClass);
        for (E f : eClass.getEnumConstants()) {
            if (hasEnum(bitSet, f)) {
                flagSet.add(f);
            }
        }
        return flagSet;
    }

    /**
     * Checks if a bit set contains a specific enum.
     *
     * @param bitSet the bit set
     * @param anEnum the enum to check
     * @return true if the bit set contains the enum, false otherwise
     */
    public static boolean hasEnum(long bitSet, Enum<?> anEnum) {
        return (bitSet & bitSetOf(anEnum)) != 0;
    }

    /**
     * Adds an enum to a bit set.
     *
     * @param bitSet the bit set
     * @param anEnum the enum to add
     * @return the updated bit set
     */
    public static long setEnum(long bitSet, Enum<?> anEnum) {
        return bitSet | bitSetOf(anEnum);
    }

    /**
     * Adds a collection of enums to a bit set.
     *
     * @param bitSet the bit set
     * @param enums  the collection of enums to add
     * @param <E>    the type of enum
     * @return the updated bit set
     */
    public static <E extends Enum<E>> long setEnums(long bitSet, Collection<E> enums) {
        if (enums == null || enums.isEmpty()) {
            return bitSet;
        }
        for (Enum<?> f : enums) {
            bitSet |= bitSetOf(f);
        }
        return bitSet;
    }

    /**
     * Removes an enum from a bit set.
     *
     * @param bitSet the bit set
     * @param anEnum the enum to remove
     * @return the updated bit set
     */
    public static long unsetEnum(long bitSet, Enum<?> anEnum) {
        return bitSet & ~bitSetOf(anEnum);
    }

    /**
     * Converts a bit set into a string representation of the corresponding EnumSet.
     *
     * @param bitSet the bit set
     * @param eClass the class of the enum
     * @param <E>    the type of enum
     * @return the string representation of the EnumSet
     */
    public static <E extends Enum<E>> String prettyPrintBitSet(long bitSet, Class<E> eClass) {
        return enumSetOf(bitSet, eClass).toString();
    }

    /**
     * Merges two bit sets.
     *
     * @param bitSet1 the first bit set
     * @param bitSet2 the second bit set
     * @return the merged bit set
     */
    public static long mergeBitSets(long bitSet1, long bitSet2) {
        return bitSet1 | bitSet2;
    }

    /**
     * Calculates the difference between two bit sets.
     *
     * @param bitSet1 the first bit set
     * @param bitSet2 the second bit set
     * @return the difference bit set
     */
    public static long diffBitSets(long bitSet1, long bitSet2) {
        return bitSet1 & ~bitSet2;
    }

    /**
     * Checks if a bit set contains all enums represented by another bit set.
     *
     * @param bitSet     the bit set to check
     * @param testBitSet the bit set representing the enums to check for
     * @return true if all enums are contained, false otherwise
     */
    public static boolean containsAll(long bitSet, long testBitSet) {
        return (bitSet & testBitSet) == testBitSet;
    }

    /**
     * Checks if a bit set contains any enum represented by another bit set.
     *
     * @param bitSet     the bit set to check
     * @param testBitSet the bit set representing the enums to check for
     * @return true if any enum is contained, false otherwise
     */
    public static boolean containsAny(long bitSet, long testBitSet) {
        return (bitSet & testBitSet) != 0;
    }

    /**
     * Checks if a bit set contains none of the enums represented by another bit set.
     *
     * @param bitSet     the bit set to check
     * @param testBitSet the bit set representing the enums to check for
     * @return true if none of the enums are contained, false otherwise
     */
    public static boolean noneOf(long bitSet, long testBitSet) {
        return (bitSet & testBitSet) == 0;
    }

    /**
     * Calculates the number of enums represented by a bit set.
     *
     * @param bitSet the bit set
     * @return the number of enums
     */
    public static int bitSetSize(long bitSet) {
        return Long.bitCount(bitSet);
    }

    /**
     * Converts a bit set into an enum array.
     *
     * @param bitSet the bit set
     * @param eClass the class of the enum
     * @param <E>    the type of enum
     * @return the enum array representing the bit set
     */
    public static <E extends Enum<E>> E[] enumArrayOf(long bitSet, Class<E> eClass) {
        if (bitSet == EMPTY_BIT_SET) {
            return null;
        }

        E[] array = (E[]) Array.newInstance(eClass, bitSetSize(bitSet));
        int i = 0;
        for (E f : eClass.getEnumConstants()) {
            if (hasEnum(bitSet, f)) {
                array[i++] = f;
            }
        }
        return array;
    }
}
