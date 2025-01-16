/*
MIT License

Copyright (c) 2024, Nuno Datia, Matilde Pato, ISEL

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package isel.sisinf.ui;

import isel.sisinf.jpa.JPAContext;
import isel.sisinf.model.Bike.*;
import isel.sisinf.model.GPS.GPS;
import isel.sisinf.model.Person.Client;
import isel.sisinf.model.Person.Manager;
import isel.sisinf.model.Person.Person;
import isel.sisinf.model.Reservation.IReservation;
import isel.sisinf.model.Reservation.Reservation;
import isel.sisinf.model.Reservation.ReservationID;
import isel.sisinf.model.Store.Store;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.postgresql.util.PSQLException;

import java.sql.Timestamp;
import java.util.*;


interface DbWorker
{
    void doWork();
}
class UI
{
    private enum Option
    {
        // DO NOT CHANGE ANYTHING!
        Unknown,
        Exit,
        createCostumer,
        listExistingBikes,
        checkBikeAvailability,
        obtainBookings,
        makeBooking,
        cancelBooking,
        about
    }
    private static UI __instance = null;
  
    private HashMap<Option,DbWorker> __dbMethods;

    private UI()
    {
        // DO NOT CHANGE ANYTHING!
        __dbMethods = new HashMap<Option,DbWorker>();
        __dbMethods.put(Option.createCostumer, () -> UI.this.createCostumer());
        __dbMethods.put(Option.listExistingBikes, () -> UI.this.listExistingBikes()); 
        __dbMethods.put(Option.checkBikeAvailability, () -> UI.this.checkBikeAvailability());
        __dbMethods.put(Option.obtainBookings, new DbWorker() {public void doWork() {UI.this.obtainBookings();}});
        __dbMethods.put(Option.makeBooking, new DbWorker() {public void doWork() {UI.this.makeBooking();}});
        __dbMethods.put(Option.cancelBooking, new DbWorker() {public void doWork() {UI.this.cancelBooking();}});
        __dbMethods.put(Option.about, new DbWorker() {public void doWork() {UI.this.about();}});

    }

    public static UI getInstance()
    {
        // DO NOT CHANGE ANYTHING!
        if(__instance == null)
        {
            __instance = new UI();
        }
        return __instance;
    }

    private Option DisplayMenu()
    {
        Option option = Option.Unknown;
        Scanner s = new Scanner(System.in); //Scanner closes System.in if you call close(). Don't do it
        try
        {
            // DO NOT CHANGE ANYTHING!
            System.out.println("Bicycle reservation");
            System.out.println();
            System.out.println("1. Exit");
            System.out.println("2. Create Costumer");
            System.out.println("3. List Existing Bikes");
            System.out.println("4. Check Bike Availability");
            System.out.println("5. Current Bookings");
            System.out.println("6. Make a booking");
            System.out.println("7. Cancel Booking");
            System.out.println("8. About");
            System.out.print(">");
            int result = s.nextInt();
            option = Option.values()[result];
        }
        catch(RuntimeException ex)
        {
            //nothing to do.
        }
        
        return option;

    }
    private static void clearConsole() throws Exception
    {
        // DO NOT CHANGE ANYTHING!
        for (int y = 0; y < 25; y++) //console is 80 columns and 25 lines
            System.out.println("\n");
    }

    public void Run() throws Exception
    {
        // DO NOT CHANGE ANYTHING!
        Option userInput;
        do
        {
            clearConsole();
            userInput = DisplayMenu();
            clearConsole();
            try
            {
                __dbMethods.get(userInput).doWork();
                System.in.read();
            }
            catch(NullPointerException ex)
            {
                //Nothing to do. The option was not a valid one. Read another.
            }

        }while(userInput!=Option.Exit);
    }

    /**
    To implement from this point forward. Do not need to change the code above.
    -------------------------------------------------------------------------------     
        IMPORTANT:
    --- DO NOT MOVE IN THE CODE ABOVE. JUST HAVE TO IMPLEMENT THE METHODS BELOW ---
    --- Other Methods and properties can be added to support implementation -------
    -------------------------------------------------------------------------------
    
    */



    private static final int TAB_SIZE = 24;

    private void createCostumer() {
        // Client: name, address, email, phone, identification, nationality
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            Client newClient = (Client) Parser.createInput(InputType.Client);
            context.getClientRepository().create(newClient);
            context.commit();
            System.out.println("Customer created with id " + newClient.getId());
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void handleException(Exception e) {
        if (e instanceof PersistenceException && e.getCause() instanceof DatabaseException) {
            String message = handleDatabaseException((PSQLException) e.getCause().getCause());
            System.out.println(message);
        } else {
            System.out.println("" + e);
        }
    }

    private String handleDatabaseException(PSQLException cause) {
        return switch (cause.getSQLState()) {
            case "23505" -> switch (cause.getServerErrorMessage().getConstraint()) {
                case "person_email_key", "store_email_key" -> "Email already in use";
                case "person_phone_key" -> "Phone number already in use";
                case "person_identification_key" -> "Identification number already in use";
                default -> "Error: " + cause;
            };
            case "23503" -> switch (cause.getServerErrorMessage().getConstraint()) {
                case "bike_gps_fkey" -> "GPS does not exist";
                case "store_manager_fkey" -> "Manager does not exist";
                case "reservation_bike_fkey" -> "Bike does not exist";
                case "reservation_store_fkey" -> "Store does not exist";
                case "reservation_client_fkey" -> "Client does not exist";
                default -> "Error: " + cause;
            };
            case "P0001" -> cause.getMessage().split("\n")[0].split(":")[1].trim();
            default -> "Unknown error: " + cause;
        };
    }

    private void listExistingBikes() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            Collection<IClassicBike> classicBikes = context.getClassicBikeRepository().findAll();
            Collection<IElectricBike> electricBikes = context.getElectricBikeRepository().findAll();
            context.commit();
            if (classicBikes.isEmpty() && electricBikes.isEmpty()) {
                throw new IllegalStateException("No bikes found");
            }
            System.out.println("-----------Classic Bikes------------");
            classicBikes.forEach(System.out::println);
            System.out.println("------------Electric Bikes-----------");
            electricBikes.forEach(System.out::println);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void checkBikeAvailability()
    {
        try (JPAContext context = new JPAContext()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the following arguments separated by commas:");
            System.out.println("Bike number, start date, end date");
            String input = scanner.nextLine();
            String[] parsed = Parser.parseString(input);
            int bikeNumber = Integer.parseInt(parsed[0]);
            Timestamp start_date = Timestamp.valueOf(parsed[1]);
            Timestamp end_date = Timestamp.valueOf(parsed[2]);
            context.beginTransaction();
            if (context.isBikeAvailable(bikeNumber, start_date, end_date)) {
                System.out.println("Bike is available from " + start_date + " to " + end_date);
            } else {
                System.out.println("Bike is not available from " + start_date + " to " + end_date);
            }
            context.commit();
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void obtainBookings() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            Collection<IReservation> reservations = context.getReservationRepository().findAll();
            if (reservations.isEmpty()) {
                throw new IllegalStateException("No bookings found");
            }
            else {
                System.out.println("-----------Bookings------------");
                reservations.forEach(System.out::println);
            }
            context.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void makeBooking()
    {
        try (JPAContext context = new JPAContext()) {
            Reservation reservation = (Reservation) Parser.createInput(InputType.Reservation);
            context.beginTransaction();
            // Check if store, client and bike have been created in the database
            if (reservation.getStoreEntity().getCode() == 0) context.getStoreRepository().create(reservation.getStoreEntity());
            if (reservation.getClient().getId() == 0) {
                if (reservation.getClient() instanceof Client) {
                    context.getClientRepository().create((Client) reservation.getClient());
                } else {
                    context.getManagerRepository().create((Manager) reservation.getClient());
                }
            }
            if (reservation.getBike().getId() == 0) {
                if (reservation.getBike() instanceof ClassicBike) {
                    context.getClassicBikeRepository().create((ClassicBike) reservation.getBike());
                } else {
                    context.getElectricBikeRepository().create((ElectricBike) reservation.getBike());
                }
            }
            context.flush();
            if (context.personHasReservation(reservation.getClient().getId(), reservation.getStart_date())) {
                throw new IllegalStateException("Client already has a reservation for that date");
            }
            reservation.getId().setStore(reservation.getStoreEntity().getCode()); // Set store id
            context.getReservationRepository().create(reservation); // bike availability is checked in the create procedure
            context.commit();
            System.out.println("Booking created with id " + reservation.getId());
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void cancelBooking()
    {
        try (JPAContext context = new JPAContext()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the store id:");
            int storeId = scanner.nextInt();
            System.out.println("Enter the reservation number:");
            int reservationNumber = scanner.nextInt();
            context.beginTransaction();
            Reservation reservation = (Reservation) context.getReservationRepository().findByKey(new ReservationID(reservationNumber, storeId));
            if (reservation == null) {
                throw new IllegalArgumentException("Reservation not found");
            }
            // To reproduce optimistic lock exception:
            // 1. Run the program in debug mode with a breakpoint at the deactivate operation
            // 2. Execute the cancelBooking operation in the program for an existing reservation
            // 3. While the program is paused at the breakpoint, execute the cancelBooking operation in another instance of the program
            // 4. Resume the debug instance of the program in order to trigger the OptimisticLockException
            context.lock(reservation, LockModeType.OPTIMISTIC);
            context.getReservationRepository().deactivate(reservation);
            context.commit();
            System.out.println("Booking cancelled");
        } catch (OptimisticLockException e) {
            System.out.println("Booking was modified by another user before cancellation");
        } catch (Exception e) {
            handleException(e);
        }
    }
    private void about()
    {
        System.out.println("GroupID: G39 LEIC43D");
        System.out.println("João Maria Gonçalves");
        System.out.println("Bernardo Pereira");
        System.out.println("Hugo Camposana");
        System.out.println("DAL version:"+ isel.sisinf.jpa.Dal.version());
        System.out.println("Core version:"+ isel.sisinf.model.Core.version());
    }
}

class App {
    public static void main(String[] args) throws Exception{
        UI.getInstance().Run();
    }
}


class Parser
{

    private static Object parseInput(String[] input, InputType type)
    {
        return switch (type) {
            case Client -> new Client(input);
            case Manager -> new Manager(input);
            case GPS -> new GPS(input);
            case ClassicBike -> new ClassicBike(input, (GPS) getInput(InputType.GPS));
            case ElectricBike -> new ElectricBike(input, (GPS) getInput(InputType.GPS));
            case Reservation -> new Reservation(input, (Bike) getBikeInput(), (Store) getInput(InputType.Store), (Person) getPersonInput());
            case Store -> new Store(input, (Manager) getInput(InputType.Manager));
        };
    }
    static String[] parseString(String input)
    {
        return Arrays.stream(input.split(",")).map(String::trim).toArray(String[]::new);
    }

    private static Object getBikeInput() {
        System.out.println("Do you wish to create a new bike? [y/n]");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("n")) {
            System.out.println("Input the bike number:");
            int bikeNumber = scanner.nextInt();
            ClassicBike classicBike = (ClassicBike) findById(new Integer[]{bikeNumber}, InputType.ClassicBike);
            if (classicBike != null) {
                return classicBike;
            }
            ElectricBike electricBike = (ElectricBike) findById(new Integer[]{bikeNumber}, InputType.ElectricBike);
            if (electricBike != null) {
                return electricBike;
            }
            throw new IllegalArgumentException("No bike found with number " + bikeNumber);
        } else {
            System.out.println("What type of bike do you want to create? [C/E]");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("C")) {
                return createInput(InputType.ClassicBike);
            } else if (input.equals("E")) {
                return createInput(InputType.ElectricBike);
            } else {
                throw new IllegalArgumentException("Invalid bike type");
            }
        }
    }

    private static Object getPersonInput() {
        System.out.println("Do you wish to create a new Client? [y/n]");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals("n")) {
            System.out.println("Input the client number:");
            int personNumber = scanner.nextInt();
            Client client = (Client) findById(new Integer[]{personNumber}, InputType.Client);
            if (client != null) {
                return client;
            }
            return findById(new Integer[]{personNumber}, InputType.Manager);
        } else {
            System.out.println("What type of person do you want to create? [C/M]");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("C")) {
                return createInput(InputType.Client);
            } else if (input.equals("M")) {
                return createInput(InputType.Manager);
            } else {
                throw new IllegalArgumentException("Invalid person type");
            }
        }
    }


    public static Object getInput(InputType type) {
        System.out.println("Do you wish to create a new " + type + "? [y/n]");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("n")) {
            return getById(type);
        } else {
            return createInput(type);
        }
    }

    public static Object createInput(InputType type) {
        System.out.println("Please input the following arguments separated by commas:");
        System.out.println(type.getArguments());
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] parsed = parseString(input);
        if (parsed.length != type.getNumberOfArguments()) {
            throw new IllegalArgumentException("Invalid number of arguments for " + type);
        }
        return parseInput(parsed, type);
    }

    public static Object getById(InputType type) {
        System.out.println("Please input the following arguments separated by commas:");
        System.out.println(type.getIdArguments());
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Integer[] id = Arrays.stream(parseString(input)).map(Integer::parseInt).toArray(Integer[]::new);
        Object obj = findById(id, type);
        if (obj == null) {
            throw new IllegalArgumentException("No " + type + " found with id " + id[0]);
        }
        return obj;
    }

    private static Object findById(Integer[] id, InputType type) {
        try (JPAContext context = new JPAContext()) {
            return switch (type) {
                case Client -> context.getClientRepository().findByKey(id[0]);
                case Manager -> context.getManagerRepository().findByKey(id[0]);
                case GPS -> context.getGPSRepository().findByKey(id[0]);
                case ClassicBike -> context.getClassicBikeRepository().findByKey(id[0]);
                case ElectricBike -> context.getElectricBikeRepository().findByKey(id[0]);
                case Reservation -> context.getReservationRepository().findByKey(new ReservationID(id[0], id[1]));
                case Store -> context.getStoreRepository().findByKey(id[0]);
            };
        } catch (Exception e) {
            System.out.println(""+e);
        }
        return null;
    }

}


enum InputType
{
    Client("[name address email phone identification nationality]", 6, "[client_id]"),
    Manager("[name address email phone identification nationality]", 6, "[manager_id]"),
    GPS("[latitude longitude battery]", 3, "[gps_id]"),
    ClassicBike("[weight model brand gear state gears]", 6, "[bike_no]"),

    ElectricBike("[weight model brand gear state autonomy speed]", 7, "[bike_no]"),

    Reservation("[start_date end_date valor]", 3, "[reservation_no store_id]" ),

    Store("[email address location phone]", 4, "[store_id]");

    private final String arguments;
    private final int numberOfArguments;

    private final String idArguments;

    InputType(String arguments, int numberOfArguments, String idArguments) {
        this.arguments = arguments;
        this.numberOfArguments = numberOfArguments;
        this.idArguments = idArguments;
    }

    public String getArguments() {
        return arguments;
    }

    public int getNumberOfArguments() {
        return numberOfArguments;
    }

    public String getIdArguments() {
        return idArguments;
    }

}

