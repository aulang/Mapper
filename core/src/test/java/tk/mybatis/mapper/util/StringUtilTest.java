/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package tk.mybatis.mapper.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.code.Style;

public class StringUtilTest {

    @Test
    public void testIsEmpty() {
        Assertions.assertTrue(StringUtil.isEmpty(null));
        Assertions.assertTrue(StringUtil.isEmpty(""));

        Assertions.assertFalse(StringUtil.isEmpty(" "));
        Assertions.assertFalse(StringUtil.isEmpty("foo"));
    }

    @Test
    public void testIsNotEmpty() {
        Assertions.assertFalse(StringUtil.isNotEmpty(null));
        Assertions.assertFalse(StringUtil.isNotEmpty(""));

        Assertions.assertTrue(StringUtil.isNotEmpty(" "));
        Assertions.assertTrue(StringUtil.isNotEmpty("foo"));
    }

    @Test
    public void testConvertByStyle() {
        Assertions.assertEquals("fOo",
                StringUtil.convertByStyle("fOo", Style.normal));
        Assertions.assertEquals("f_oo",
                StringUtil.convertByStyle("fOo", Style.camelhump));
        Assertions.assertEquals("FOO",
                StringUtil.convertByStyle("fOo", Style.uppercase));
        Assertions.assertEquals("foo",
                StringUtil.convertByStyle("FoO", Style.lowercase));
        Assertions.assertEquals("fo_o",
                StringUtil.convertByStyle("FoO", Style.camelhumpAndLowercase));
        Assertions.assertEquals("F_OO",
                StringUtil.convertByStyle("fOo", Style.camelhumpAndUppercase));
    }

    @Test
    public void testCamelhumpToUnderline() {
        Assertions.assertEquals("foo", StringUtil.camelhumpToUnderline("foo"));
        Assertions.assertEquals("f_oo", StringUtil.camelhumpToUnderline("fOo"));
    }

    @Test
    public void testUnderlineToCamelhump() {
        Assertions.assertEquals("foo", StringUtil.underlineToCamelhump("foo"));
        Assertions.assertEquals("foo", StringUtil.underlineToCamelhump("Foo"));
    }

    @Test
    public void testIsUppercaseAlpha() {
        Assertions.assertTrue(StringUtil.isUppercaseAlpha('F'));

        Assertions.assertFalse(StringUtil.isUppercaseAlpha('f'));
    }

    @Test
    public void testIsLowercaseAlpha() {
        Assertions.assertTrue(StringUtil.isLowercaseAlpha('f'));

        Assertions.assertFalse(StringUtil.isLowercaseAlpha('F'));
    }

    @Test
    public void testToUpperAscii() {
        Assertions.assertEquals('F', StringUtil.toUpperAscii('f'));
        Assertions.assertEquals('F', StringUtil.toUpperAscii('F'));
    }

    @Test
    public void testToLowerAscii() {
        Assertions.assertEquals('f', StringUtil.toLowerAscii('f'));
        Assertions.assertEquals('f', StringUtil.toLowerAscii('F'));
    }
}
