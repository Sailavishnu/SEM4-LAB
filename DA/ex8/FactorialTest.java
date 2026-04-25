class MenuSystem {

    public static void main(String[] args) {
        int choice = 2;
        int count = 0;

        while (count < 2) {
            switch (choice) {
                case 1:
                    System.out.println("View Balance");
                    break;
                case 2:
                    System.out.println("Withdraw");
                    break;
                case 3:
                    System.out.println("Deposit");
                    break;
                default:
                    System.out.println("Invalid Option");
            }
            count++;
        }
    }
}








class AccessSystem {

    public static void main(String[] args) {
        int attempts = 0;
        boolean isAdmin = false;
        int pin = 1234;
        int input = 1111;

        while (attempts < 2) {
            if (input == pin) {
                if (isAdmin)
                    System.out.println("Admin Access");
                else
                    System.out.println("User Access");
                break;
            } else {
                attempts++;
            }
        }

        if (attempts == 2)
            System.out.println("Blocked");
    }
}