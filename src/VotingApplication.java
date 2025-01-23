import entities.Candidate;
import entities.Election;
import entities.User;
import entities.VoteRecord;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import services.*;

public class VotingApplication {
    private final Scanner scanner;
    private final UserService userService;
    private final ElectionService electionService;
    private final BlockchainService blockchainService;
    private User currentUser;

    public VotingApplication() {
        DatabaseService dbService = new DatabaseService();
        this.scanner = new Scanner(System.in);
        this.userService = new UserService(dbService);
        this.electionService = new ElectionService(dbService);
        this.blockchainService = new BlockchainService(dbService);
    }

    public static void main(String[] args) {
        new VotingApplication().start();
    }

    public void start() {
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else if ("ADMIN".equals(currentUser.getRole())) {
                showAdminMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("Welcome to Blockchain-based Voting Application\n");

        System.out.println("1. Login");
        System.out.println("2. Exit");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    private void showAdminMenu() {
        System.out.println("\n1. Register New User");
        System.out.println("2. Generate Voter ID");
        System.out.println("3. Create Election");
        System.out.println("4. Add Candidate");
        System.out.println("5. Verify Blockchain");
        System.out.println("6. Logout");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                registerUser();
                break;
            case 2:
                generateVoterId();
                break;
            case 3:
                createElection();
                break;
            case 4:
                addCandidate();
                break;
            case 5:
                verifyBlockchain();
                break;
            case 6:
                logout();
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    private void showUserMenu() {
        System.out.println("\n1. View Active Elections");
        System.out.println("2. Cast Vote");
        System.out.println("3. View My Voting History");
        System.out.println("4. Logout");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                viewActiveElections();
                break;
            case 2:
                castVote();
                break;
            case 3:
                viewVotingHistory();
                break;
            case 4:
                logout();
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    private void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        currentUser = userService.authenticate(username, password);
        if (currentUser == null) {
            System.out.println("Invalid credentials!");
        } else {
            System.out.println("Welcome, " + currentUser.getUsername() + "!");
        }
    }

    private void logout() {
        currentUser = null;
        System.out.println("Logged out successfully!");
    }

    private void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role (ADMIN/USER): ");
        String role = scanner.nextLine().toUpperCase();

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setRole(role);

        try {
            userService.registerUser(newUser, password);
            System.out.println("User registered successfully!");
        } catch (RuntimeException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void generateVoterId() {
        System.out.print("Enter user ID: ");
        Long userId = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        try {
            userService.generateVoterId(userId);
            System.out.println("Voter ID generated successfully!");
        } catch (RuntimeException e) {
            System.out.println("Voter ID generation failed: " + e.getMessage());
        }
    }

    private void createElection() {
        System.out.print("Enter election title: ");
        String title = scanner.nextLine();
        System.out.print("Enter start date (YYYY-MM-DD HH:mm): ");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter end date (YYYY-MM-DD HH:mm): ");
        String endDateStr = scanner.nextLine();

        try {
            Election election = new Election();
            election.setTitle(title);
            election.setStartDate(LocalDateTime.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            election.setEndDate(LocalDateTime.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            election.setCreatedBy(currentUser.getId());

            electionService.createElection(election);
            System.out.println("Election created successfully!");
        } catch (Exception e) {
            System.out.println("Election creation failed: " + e.getMessage());
        }
    }

    private void addCandidate() {
        System.out.print("Enter election ID: ");
        Long electionId = scanner.nextLong();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter candidate name: ");
        String name = scanner.nextLine();

        try {
            Candidate candidate = new Candidate();
            candidate.setName(name);
            candidate.setElectionId(electionId);

            electionService.addCandidate(candidate);
            System.out.println("Candidate added successfully!");
        } catch (RuntimeException e) {
            System.out.println("Failed to add candidate: " + e.getMessage());
        }
    }

    private void viewActiveElections() {
        try {
            List<Election> activeElections = electionService.getActiveElections();
            if (activeElections.isEmpty()) {
                System.out.println("No active elections found.");
                return;
            }

            System.out.println("\nActive Elections:");
            for (Election election : activeElections) {
                System.out.println("ID: " + election.getId() +
                        " | Title: " + election.getTitle() +
                        " | Ends: " + election.getEndDate());

                List<Candidate> candidates = electionService.getCandidates(election.getId());
                System.out.println("Candidates:");
                for (Candidate candidate : candidates) {
                    System.out.println("  - ID: " + candidate.getId() +
                            " | Name: " + candidate.getName());
                }
                System.out.println();
            }
        } catch (RuntimeException e) {
            System.out.println("Failed to retrieve elections: " + e.getMessage());
        }
    }

    private void castVote() {
        if (!currentUser.isVoterIdValid()) {
            System.out.println("Your voter ID is not valid. Please contact an admin.");
            return;
        }

        System.out.print("Enter election ID: ");
        Long electionId = scanner.nextLong();
        System.out.print("Enter candidate ID: ");
        Long candidateId = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        try {
            if (blockchainService.hasVoted(currentUser.getVoterId(), electionId)) {
                System.out.println("You have already voted in this election!");
                return;
            }

            if (!electionService.isElectionActive(electionId)) {
                System.out.println("This election is not currently active!");
                return;
            }

            // Get user's private key for signing
            PrivateKey privateKey = userService.getUserPrivateKey(currentUser.getId());
            blockchainService.castVote(currentUser.getVoterId(), electionId, candidateId, privateKey);
            System.out.println("Vote cast successfully!");
        } catch (RuntimeException e) {
            System.out.println("Failed to cast vote: " + e.getMessage());
        }
    }

    private void viewVotingHistory() {
        try {
            List<VoteRecord> history = blockchainService.getVotingHistory(currentUser.getVoterId());
            if (history.isEmpty()) {
                System.out.println("No voting history found.");
                return;
            }

            System.out.println("\nYour Voting History:");
            for (VoteRecord record : history) {
                System.out.println("Election: " + record.getElectionTitle() +
                        " | Date: " + record.getTimestamp() +
                        " | Candidate: " + record.getCandidateName());
            }
        } catch (RuntimeException e) {
            System.out.println("Failed to retrieve voting history: " + e.getMessage());
        }
    }

    private void verifyBlockchain() {
        try {
            boolean isValid = blockchainService.verifyBlockchain();
            if (isValid) {
                System.out.println("Blockchain verification successful! All votes are valid.");
            } else {
                System.out.println("WARNING: Blockchain verification failed! Vote tampering detected.");
            }
        } catch (RuntimeException e) {
            System.out.println("Verification failed: " + e.getMessage());
        }
    }
}