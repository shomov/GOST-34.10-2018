// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package gost.signature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public record Point(BigInteger x, BigInteger y) {

    @Override
    public String toString() {
        return "Xp = " + x() + " (коэффициент точки эллиптической кривой)\n"
                + "Yp = " + y() + " (коэффициент точки эллиптичекой кривой)\n";
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Point(
            @JsonProperty("x") BigInteger x,
            @JsonProperty("y") BigInteger y) {
        this.x = x;
        this.y = y;
    }
}
