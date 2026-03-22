package br.com.medflow.entities.base;

import java.math.BigDecimal;
import java.util.Objects;

public final class DomainValidation {

  private DomainValidation() {}

  public static <T> T required(T value, String message) {
    return Objects.requireNonNull(value, message);
  }

  public static String requiredNormalizedText(String value, String requiredMessage) {
    return required(value, requiredMessage).strip();
  }

  public static String requiredText(String value, String requiredMessage) {
    return requiredText(value, requiredMessage, requiredMessage);
  }

  public static String requiredText(String value, String requiredMessage, String blankMessage) {
    String normalized = requiredNormalizedText(value, requiredMessage);
    if (normalized.isEmpty()) {
      throw new IllegalArgumentException(blankMessage);
    }
    return normalized;
  }

  public static String optionalText(String value) {
    if (value == null) {
      return null;
    }
    String normalized = value.strip();
    return normalized.isEmpty() ? null : normalized;
  }

  public static BigDecimal requiredNonNegative(
      BigDecimal value, String requiredMessage, String negativeMessage) {
    BigDecimal requiredValue = required(value, requiredMessage);
    if (requiredValue.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException(negativeMessage);
    }
    return requiredValue;
  }

  public static BigDecimal requiredPositive(
      BigDecimal value, String requiredMessage, String nonPositiveMessage) {
    BigDecimal requiredValue = required(value, requiredMessage);
    if (requiredValue.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException(nonPositiveMessage);
    }
    return requiredValue;
  }

  public static BigDecimal optionalInRange(
      BigDecimal value,
      BigDecimal minInclusive,
      BigDecimal maxInclusive,
      String outOfRangeMessage) {
    if (value == null) {
      return null;
    }
    if (value.compareTo(minInclusive) < 0 || value.compareTo(maxInclusive) > 0) {
      throw new IllegalArgumentException(outOfRangeMessage);
    }
    return value;
  }

  public static <T extends Comparable<? super T>> void requireStrictlyBefore(
      T start, T end, String message) {
    if (start.compareTo(end) >= 0) {
      throw new IllegalArgumentException(message);
    }
  }

  public static <T extends Comparable<? super T>> void requireNotBefore(
      T value, T reference, String message) {
    if (value != null && reference != null && value.compareTo(reference) < 0) {
      throw new IllegalArgumentException(message);
    }
  }
}
