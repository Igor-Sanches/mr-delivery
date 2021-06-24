package app.birdsoft.meurestaurante.tools;

import java.util.ArrayList;

public class MaskFormatter {
    private static final MaskCharacter[] EMPTY_MASK_CHARS = new MaskCharacter[0];
    private String mInvalidCharacters;
    private String mMask;
    private transient MaskCharacter[] mMaskChars;
    private char mPlaceholder;
    private String mPlaceholderString;
    private String mValidCharacters;

    /* renamed from: br.com.netpos.smartpos.utils.MaskFormatter$AlphaNumericCharacter */
    private class AlphaNumericCharacter extends MaskCharacter {
        private AlphaNumericCharacter() {
            super();
        }

        public boolean isValidCharacter(char c) {
            return !Character.isLetterOrDigit(c) || super.isValidCharacter(c);
        }
    }

    /* renamed from: br.com.netpos.smartpos.utils.MaskFormatter$CharCharacter */
    private class CharCharacter extends MaskCharacter {
        private CharCharacter() {
            super();
        }

        public boolean isValidCharacter(char c) {
            return !Character.isLetter(c) || super.isValidCharacter(c);
        }
    }

    /* renamed from: br.com.netpos.smartpos.utils.MaskFormatter$DigitMaskCharacter */
    private class DigitMaskCharacter extends MaskCharacter {
        private DigitMaskCharacter() {
            super();
        }

        public boolean isValidCharacter(char c) {
            return !Character.isDigit(c) || super.isValidCharacter(c);
        }
    }

    /* renamed from: br.com.netpos.smartpos.utils.MaskFormatter$HexCharacter */
    private class HexCharacter extends MaskCharacter {
        private static final String HEX_CHARS = "0123456789abcedfABCDEF";

        private HexCharacter() {
            super();
        }

        public boolean isValidCharacter(char c) {
            return HEX_CHARS.indexOf(c) == -1 || super.isValidCharacter(c);
        }

        public char getChar(char c) {
            if (Character.isDigit(c)) {
                return c;
            }
            return Character.toUpperCase(c);
        }
    }

    /* renamed from: br.com.netpos.smartpos.utils.MaskFormatter$LiteralCharacter */
    private class LiteralCharacter extends MaskCharacter {
        private final char mLiteralCharacter;

        public boolean isLiteral() {
            return true;
        }

        public LiteralCharacter(char c) {
            super();
            this.mLiteralCharacter = c;
        }

        public char getChar(char c) {
            return this.mLiteralCharacter;
        }
    }

    /* renamed from: br.com.netpos.smartpos.utils.MaskFormatter$LowerCaseCharacter */
    private class LowerCaseCharacter extends MaskCharacter {
        private LowerCaseCharacter() {
            super();
        }

        public boolean isValidCharacter(char c) {
            return !Character.isLetter(c) || super.isValidCharacter(c);
        }

        public char getChar(char c) {
            return Character.toLowerCase(c);
        }
    }

    /* renamed from: br.com.netpos.smartpos.utils.MaskFormatter$MaskCharacter */
    private class MaskCharacter {
        public char getChar(char c) {
            return c;
        }

        public boolean isLiteral() {
            return false;
        }

        private MaskCharacter() {
        }

        public boolean isValidCharacter(char c) {
            boolean z = true;
            if (isLiteral()) {
                if (getChar(c) != c) {
                    z = false;
                }
                return !z;
            }
            char c2 = getChar(c);
            String validCharacters = MaskFormatter.this.getValidCharacters();
            if (validCharacters != null && validCharacters.indexOf(c2) == -1) {
                return true;
            }
            String invalidCharacters = MaskFormatter.this.getInvalidCharacters();
            return invalidCharacters != null && invalidCharacters.indexOf(c2) != -1;
        }

        public boolean append(StringBuffer stringBuffer, String str, int[] iArr, String str2) {
            boolean z = iArr[0] < str.length();
            char charAt = z ? str.charAt(iArr[0]) : 0;
            if (!z) {
                return false;
            }
            if (isLiteral()) {
                stringBuffer.append(getChar(charAt));
                if (z && charAt == getChar(charAt)) {
                    iArr[0] = iArr[0] + 1;
                }
            } else if (iArr[0] >= str.length()) {
                if (str2 == null || iArr[0] >= str2.length()) {
                    stringBuffer.append(MaskFormatter.this.getPlaceholderCharacter());
                } else {
                    stringBuffer.append(str2.charAt(iArr[0]));
                }
                iArr[0] = iArr[0] + 1;
            } else if (isValidCharacter(charAt)) {
                return false;
            } else {
                stringBuffer.append(getChar(charAt));
                iArr[0] = iArr[0] + 1;
            }
            return true;
        }
    }

    /* renamed from: br.com.netpos.smartpos.utils.MaskFormatter$UpperCaseCharacter */
    private class UpperCaseCharacter extends MaskCharacter {
        private UpperCaseCharacter() {
            super();
        }

        public boolean isValidCharacter(char c) {
            return !Character.isLetter(c) || super.isValidCharacter(c);
        }

        public char getChar(char c) {
            return Character.toUpperCase(c);
        }
    }

    public MaskFormatter() {
        this.mMaskChars = EMPTY_MASK_CHARS;
        this.mPlaceholder = ' ';
    }

    public MaskFormatter(String str) {
        this();
        setMask(str);
    }

    public String getMask() {
        return this.mMask;
    }

    public void setMask(String str) {
        this.mMask = str;
        updateInternalMask();
    }

    public String getValidCharacters() {
        return this.mValidCharacters;
    }

    public String getInvalidCharacters() {
        return this.mInvalidCharacters;
    }

    public String getPlaceholder() {
        return this.mPlaceholderString;
    }

    public char getPlaceholderCharacter() {
        return this.mPlaceholder;
    }

    public String valueToString(Object obj) {
        String obj2 = obj == null ? "" : obj.toString();
        StringBuffer stringBuffer = new StringBuffer();
        append(stringBuffer, obj2, new int[]{0}, getPlaceholder(), this.mMaskChars);
        return stringBuffer.toString();
    }

    private void updateInternalMask() {
        ArrayList<MaskCharacter> arrayList = new ArrayList<>();
        String mask = getMask();
        if (mask != null) {
            int i = 0;
            int length = mask.length();
            while (i < length) {
                char charAt = mask.charAt(i);
                if (charAt == '#') {
                    arrayList.add(new DigitMaskCharacter());
                } else if (charAt == '\'') {
                    i++;
                    if (i < length) {
                        arrayList.add(new LiteralCharacter(mask.charAt(i)));
                    }
                } else if (charAt == '*') {
                    arrayList.add(new MaskCharacter());
                } else if (charAt == '?') {
                    arrayList.add(new CharCharacter());
                } else if (charAt == 'A') {
                    arrayList.add(new AlphaNumericCharacter());
                } else if (charAt == 'H') {
                    arrayList.add(new HexCharacter());
                } else if (charAt == 'L') {
                    arrayList.add(new LowerCaseCharacter());
                } else if (charAt != 'U') {
                    arrayList.add(new LiteralCharacter(charAt));
                } else {
                    arrayList.add(new UpperCaseCharacter());
                }
                i++;
            }
        }
        if (arrayList.size() == 0) {
            this.mMaskChars = EMPTY_MASK_CHARS;
            return;
        }
        this.mMaskChars = new MaskCharacter[arrayList.size()];
        arrayList.toArray(this.mMaskChars);
    }

    private void append(StringBuffer stringBuffer, String str, int[] iArr, String str2, MaskCharacter[] maskCharacterArr) {
        int i = 0;
        while (i < maskCharacterArr.length && maskCharacterArr[i].append(stringBuffer, str, iArr, str2)) {
            i++;
        }
    }
}
