import java.util.*;
import java.io.*;
import java.util.logging.*;

public class Main {
    // 创建日志记录器
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static class Minion {
        int health;
        int attack;

        Minion(int health, int attack) {
            this.health = health;
            this.attack = attack;
        }
    }

    public static class Player {
        int heroHealth;
        List<Minion> minions;
        int currentPlayer;

        Player(int heroHealth) {
            this.heroHealth = heroHealth;
            this.minions = new ArrayList<>();
            this.currentPlayer = 0;
        }

        void performAttack(int attackerPos, int targetPos) {
            Minion attacker = minions.get(attackerPos - 1);
            Minion target = minions.get(targetPos - 1);

            target.health -= attacker.attack;
            if (target.health <= 0) {
                minions.remove(targetPos - 1);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // 配置日志记录器
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.INFO); // 设置日志级别为INFO

        // 创建一个控制台处理程序，将日志消息输出到控制台
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO); // 设置控制台处理程序的日志级别
        logger.addHandler(consoleHandler);

        Scanner scanner = new Scanner(System.in);
        Player player1 = new Player(30);
        Player player2 = new Player(30);
        int numberOfTurns = scanner.nextInt();
        int currentPlayer = 0;

        while (numberOfTurns-- > 0) {
            String command = scanner.next();
            if (command.equals("summon")) {
                int position = scanner.nextInt();
                int attack = scanner.nextInt();
                int health = scanner.nextInt();
                Minion summonedMinion = new Minion(health, attack);
                if (currentPlayer == 0) {
                    player1.minions.add(position - 1, summonedMinion);
                } else {
                    player2.minions.add(position - 1, summonedMinion);
                }
                logger.log(Level.INFO, "Summoned a minion at position {0}, attack: {1}, health: {2}", new Object[]{position, attack, health});
            } else if (command.equals("attack")) {
                int attackerPos = scanner.nextInt();
                int targetPos = scanner.nextInt();
                if (currentPlayer == 0) {
                    if (targetPos == 0) {
                        player2.heroHealth -= player1.minions.get(attackerPos - 1).attack;
                    } else {
                        player1.performAttack(attackerPos, targetPos);
                        player2.performAttack(targetPos, attackerPos);
                    }
                } else {
                    if (targetPos == 0) {
                        player1.heroHealth -= player2.minions.get(attackerPos - 1).attack;
                    } else {
                        player2.performAttack(attackerPos, targetPos);
                        player1.performAttack(targetPos, attackerPos);
                    }
                }
                logger.log(Level.INFO, "Minion attacked! Target health reduced to {0}", player1.minions.get(targetPos - 1).health);
            } else if (command.equals("end")) {
                currentPlayer = 1 - currentPlayer; // Switch player
            }
        }

        if (player1.heroHealth > 0 && player2.heroHealth > 0) {
            System.out.println("0");
        } else if (player1.heroHealth > 0) {
            System.out.println("1");
        } else if (player2.heroHealth > 0) {
            System.out.println("-1");
        }

        System.out.println(player1.heroHealth);
        System.out.printf("%d ", player1.minions.size());
        for (Minion minion : player1.minions) {
            System.out.printf("%d ", minion.health);
        }
        System.out.println();

        System.out.println(player2.heroHealth);
        System.out.printf("%d ", player2.minions.size());
        for (Minion minion : player2.minions) {
            System.out.printf("%d ", minion.health);
        }
        System.out.println();
    }
}
