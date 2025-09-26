public class VowelOrConsonants {
    public static void main(String[] args) {
        char character='a';
        switch (character) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
            case 'A':
            case 'E':
            case 'I':
            case 'O':
            case 'U':
                System.out.println(character + " is a vowel");
                
                break;
            default:
                System.out.println(character);
        }

    }
}
