import java.io.*;

/**
 * Handles batch input processing using properties file.
 */
public class BankingUI {
  private boolean done = false;
  private boolean authenticated = false;
  private boolean admin = false;
  private String currentCustomer = "";
  private String input;
  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Display Menu
	 *
	 */
	public void start() {
		while(!done) {
      System.out.println("\nMain Menu");
      System.out.println("1. New Customer");
      System.out.println("2. Customer Login");
      System.out.println("3. Exit");
      System.out.print("Enter Option Number:");
      menuOptions(readLine());
      while(authenticated) {
        System.out.println("\nCustomer Main Menu");
        System.out.println("1. Open Account");
        System.out.println("2. Close Account");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. Transfer");
        System.out.println("6. Account Summary");
        System.out.println("7. Exit");
        System.out.print("Enter Option Number:");
        customerMenuOptions(readLine());
      }
      while(admin) {
        System.out.println("\nAdministrator Main Menu");
        System.out.println("1. Account Summary for Customer");
        System.out.println("2. Report A :: Customer Information with Total Balance in Decreasing Order");
        System.out.println("3. Report B :: Find the Average Total Balance Between Age Groups");
        System.out.println("4. Exit");
        System.out.print("Enter Option Number:");
        administratorMenuOptions(readLine());
      }
    }
	}

  private void menuOptions(String menuChoice) {
    boolean valid = false;
      String name ="";
      String gender = "M";
      String age = "0";
      String pin = "0";
      String id = "0";

    switch(menuChoice) {
      case "1":
        System.out.print("\nEnter Name:");
        name = readLine();
        valid = false;
        while(!valid) {
          System.out.print("Enter Gender(M/F):");
          gender = readLine();
          valid = validateInput(gender, "gender");
        }
        valid = false;
        while(!valid) {
          System.out.print("Enter Age:");
          age = readLine();
          valid = validateInput(age, "number");
        }
        valid = false;
        while(!valid) {
          System.out.print("Enter Positive PIN:");
          pin = readLine();
          valid = validateInput(pin, "number");
        }
        if(valid) {
          System.out.println("\nUpdating Database");
          BankingSystem.newCustomer(name, gender, age, pin);
        }
        break;
      case "2":
        System.out.println("\nLog In");
        valid = false;
        while(!valid) {
          System.out.print("Enter Customer ID:");
          id = readLine();
          valid = validateInput(id, "number");
        }
        valid = false;
        while(!valid) {
          System.out.print("Enter PIN:");
          pin = readLine();
          valid = validateInput(pin, "number");
        }

        if(valid) {
          System.out.println("\nQuerying Database");
          valid = BankingSystem.authCustomer(id, pin);
          if(id.equalsIgnoreCase("0") && pin.equalsIgnoreCase("0")) {
            admin = true;
          }
          if (valid) {
            currentCustomer = id;
            authenticated = true;
          }
        }
        break;
      case "3":
        System.out.println("Bye!\n");
        done = true;
        break;
      default:
        System.out.println("Not an Option\n");
        break;
    }
  }

  private void customerMenuOptions(String menuChoice) {
    boolean valid = false;
      String name ="";
      String gender = "M";
      String age = "0";
      String pin = "0";
      String id = "0";
      String accountType = "0";
      String deposit = "0";
      String amount = "0";
      String number = "0";

    switch(menuChoice) {
      case "1":
        valid = false;
        while(!valid) {
          System.out.print("\nEnter Customer ID:");
          id = readLine();
          valid = validateInput(id, "number");
        }
        valid = false;
        while(!valid) {
          System.out.print("Account Type(S/C):");
          accountType = readLine();
          valid = validateInput(accountType, "account");
        }
        valid = false;
        while(!valid) {
          System.out.print("Enter Initial Deposit:");
          deposit = readLine();
          valid = validateInput(deposit, "number");
        }
        if(valid) {
          System.out.println("\nUpdating Database");
          BankingSystem.openAccount(id, accountType, deposit);
        }
        break;
      case "2":
        valid = false;
        while(!valid) {
          System.out.print("\nEnter Account #:");
          number = readLine();
          valid = validateInput(number, "number");
        }
        if(valid) {
          System.out.println("\nUpdating Database");
          if (BankingSystem.findAccount(currentCustomer,number)) {
            BankingSystem.closeAccount(number);
          }
        }
        break;
      case "3":
        valid = false;
        while(!valid) {
          System.out.print("\nEnter Account #:");
          number = readLine();
          valid = validateInput(number, "number");
        }
        valid = false;
        while(!valid) {
          System.out.print("Deposit Amount:");
          amount = readLine();
          valid = validateInput(amount, "number");
        }
        if(valid) {
          System.out.println("\nUpdating Database");
          BankingSystem.deposit(number, amount);
        }
        break;
      case "4":
        valid = false;
        while(!valid) {
          System.out.print("\nEnter Account #:");
          number = readLine();
          valid = validateInput(number, "number");
        }
        valid = false;
        while(!valid) {
          System.out.print("Withdraw Amount:");
          amount = readLine();
          valid = validateInput(amount, "number");
        }
        if(valid) {
          System.out.println("\nUpdating Database");
          BankingSystem.withdraw(number, amount);
        }
        break;
      case "5":
        valid = false;
        while(!valid) {
          System.out.print("\nEnter Source Account #:");
          id = readLine();
          valid = validateInput(id, "number");
        }
        valid = false;
        while(!valid) {
          System.out.print("Enter Destination Account #:");
          number = readLine();
          valid = validateInput(number, "number");
        }
        valid = false;
        while(!valid) {
          System.out.print("Enter Transfer Amount:");
          deposit = readLine();
          valid = validateInput(deposit, "number");
        }
        if(valid) {
          System.out.println("\nUpdating Database");
          if (BankingSystem.findAccount(currentCustomer,id)) {
            BankingSystem.transfer(id, number, deposit);
          }
        }
        break;
      case "6":
        System.out.println("\nQuerying Database");
        BankingSystem.accountSummary(currentCustomer);
        break;
      case "7":
        System.out.println("Logging Out\n");
        currentCustomer = "";
        authenticated = false;
        break;
      default:
        break;
    }
  }

  private void administratorMenuOptions(String menuChoice) {
      boolean valid = false;
      String id = "0";
      String ageMin = "0";
      String ageMax = "0";


      switch(menuChoice) {
        case "1":
          valid = false;
          while(!valid) {
            System.out.print("\nEnter Customer ID:");
            id = readLine();
            valid = validateInput(id, "number");
          }
          if(valid) {
            System.out.println("\nQuerying Database");
            BankingSystem.accountSummary(id);
          }
          break;
        case "2":
          System.out.println("\nQuerying Database");
          BankingSystem.reportA();
          break;
        case "3":
          valid = false;
          while(!valid) {
            System.out.print("\nEnter Min Age:");
            ageMin = readLine();
            valid = validateInput(ageMin, "number");
          }
          valid = false;
          while(!valid) {
            System.out.print("\nEnter Max Age:");
            ageMax = readLine();
            valid = validateInput(ageMax, "number");
          }
          if(valid) {
            System.out.println("\nQuerying Database");
            BankingSystem.reportB(ageMin, ageMax);
          }
          break;
        case "4":
          System.out.println("Logging Out\n");
          admin = false;
          break;
        default:
          break;
      }
  }

  private boolean validateInput(String input, String type) {
    boolean valid = false;
    switch(type) {
      case "gender":
        if(input.equalsIgnoreCase("m") || input.equalsIgnoreCase("f")) {
          valid = true;
        }
        break;
      case "number":
        if(Integer.parseInt(input) >= 0){
          valid = true;
        }
        break;
      case "account":
        if(input.equalsIgnoreCase("c") || input.equalsIgnoreCase("s")) {
          valid = true;
        }
        break;
      case "self":
        if (input != currentCustomer) {
          System.out.println("Unauthorized");
          valid = false;
        } else {
          valid = true;
        }
        break;
      default:
        break;
    }
    return valid;
  }

  private String readLine() {
    try {
      input = reader.readLine();
      return input;
    } catch (Exception err) {
      System.out.println(err);
    }
    return null;
  }


}
