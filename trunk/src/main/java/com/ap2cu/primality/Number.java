package com.ap2cu.primality;

public class Number extends PrimeNumberAdapter {

  protected static PrimeNumberList primeNumbers = new PrimeNumberList();
  protected Long number;

  public Number(final Integer number) {
    this(number.longValue());
  }

  public Number(final Long number) {
    this.number = number;
    addPrimeNumberListener(this);
  }

  public void onPrimeNumberFound(PrimeNumberEvent event) {
    Long n = event.getNumber().getValue();
    if (!primeNumbers.containsKey(n))
      primeNumbers.addPrimeNumber(n);
  }

  public Long getValue() {
    return number;
  }

  public boolean isDivisibleBy(long divisor) {
    return number % divisor == 0;
  }

  public boolean isPrime() {
    return isPrime(number);
  }
  
  private boolean isPrime(long number) {
    if (number == 1 || number == 0)
      return false;
    for (long i = 2; i <= Math.sqrt(number); i++) {
      if (number % i == 0)
        return false;
    }
    notifyPrimeNumberFound(number);
    return true;
  }

  public Number divideBy(long divisor) {
    return new Number(number / divisor);
  }

  public PrimeNumberList decompose() {
    return decompose(2, new PrimeNumberList());
  }

  private PrimeNumberList decomposeDividend(long divisor, PrimeNumberList divisors) {
    divisors.addPrimeNumber(divisor);
    return divideBy(divisor).decompose(divisor, divisors);
  }
  
  public PrimeNumberList decompose(long seed, PrimeNumberList divisors) {
    long divisor = seed;
    if (seed == 2)
      if(isDivisibleBy(divisor))
        return decomposeDividend(divisor, divisors);
      else
        divisor++;
    for (; divisor <= Math.sqrt(number); divisor += 2)
      if (isPrime(divisor) && isDivisibleBy(divisor))
        return decomposeDividend(divisor, divisors);
    divisors.addPrimeNumber(number);
    return divisors;
  }

  public String toString() {
    return number.toString();
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    long time = System.currentTimeMillis();
//    Number number = new Number(67571703108L);// 2(2) x 3(2) x 1876991753(1)
    // Number number = new Number(1876991753);//7(1) x 268141679(1)
    // Number number = new Number(268141679);//13(1) x 20626283(1)
    // Number number = new Number(20626283);//43(1) x 479681(1)
    // Number number = new Number(479681);//4483(1) x 107(1)
    // Number number = new Number(4483);//4483(1)
//     Number number = new Number(Integer.MAX_VALUE);//2147483647(1)
//     Number number = new Number(80449947769L);//1(1) x 283637(2)
//     Number number = new Number(20097289);//1(1) x 4483(2)
    // Number number = new Number(19335923178L);//2(1) x 3(1) x 3222653863(1)
//     Number number = new Number(1271544671);//4483(1) x 283637(1)
     Number number = new Number(456);//45673(1) x
     System.out.println(number.isPrime());
    // 22397412556193(1)
    System.out.print("Prime Divisors of "+number+": ");
    System.out.println(number.decompose());
    System.err.println(System.currentTimeMillis() - time);
  }

}
