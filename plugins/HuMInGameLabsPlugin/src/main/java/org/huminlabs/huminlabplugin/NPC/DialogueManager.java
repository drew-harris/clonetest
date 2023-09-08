package org.huminlabs.huminlabplugin.NPC;

import com.google.gson.Gson;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import static org.huminlabs.huminlabplugin.HuMInLabPlugin.plugin;

/**
 * The DialogueManager class is responsible for managing the dialogues of NPCs in the game.
 * It loads the dialogues from JSON files, organizes them into different categories, and provides methods
 * to retrieve and run dialogues for specific actors and units.
 */
public class DialogueManager {

    Gson gson = new Gson();
    private ArrayList<Dialogue> serenityDialogues;
    private ArrayList<Dialogue> victoriaDialogues;
    private ArrayList<Dialogue> zionDialogues;
    private ArrayList<Dialogue> mayorDialogues;
    private ArrayList<Dialogue> santiagoDialogues;
    private ArrayList<Dialogue> u1TutorialDialogues;
    private ArrayList<Dialogue> u2TutorialDialogues;

    private static String prefix = "/static";

    /**
     * Constructs a new DialogueManager and loads the dialogues from JSON files.
     *
     * @throws FileNotFoundException if the dialogue files are not found.
     */
    public DialogueManager() throws FileNotFoundException {
        serenityDialogues = new ArrayList<>();
        victoriaDialogues = new ArrayList<>();
        zionDialogues = new ArrayList<>();
        mayorDialogues = new ArrayList<>();
        santiagoDialogues = new ArrayList<>();
        u1TutorialDialogues = new ArrayList<>();
        u2TutorialDialogues = new ArrayList<>();

        loadDialogues();
    }

    /**
     * Loads the dialogues from JSON files and organizes them into different categories.
     *
     * @throws FileNotFoundException if the dialogue files are not found.
     */
    public void loadDialogues() throws FileNotFoundException {
        Gson gson = new Gson();

        // Load unit 1 dialogues
        Dialogue[] unit1Dialogues;
        File file = new File(prefix + "/NPC-Dialogue/Unit_1_Dialogue.json");
        if (file.exists()) {
            Reader reader = new FileReader(file);
            unit1Dialogues = gson.fromJson(reader, Dialogue[].class);

            for (Dialogue dialogue : unit1Dialogues) {
                dialogue.setUnit("1");
            }

            System.out.println("Unit 1 loaded: " + unit1Dialogues.length);
            skimJson(unit1Dialogues);
        } else {
            System.out.println("Dialogue file not found!");
            System.out.println(prefix + "/NPC-Dialogue/Unit_1_Dialogue.json");
        }

        // Load unit 2 dialogues
        Dialogue[] unit2Dialogues;
        file = new File(prefix + "/NPC-Dialogue/Unit_2_Dialogue.json");
        if (file.exists()) {
            Reader reader = new FileReader(file);
            unit2Dialogues = gson.fromJson(reader, Dialogue[].class);

            for (Dialogue dialogue : unit2Dialogues) {
                dialogue.setUnit("2");
            }

            skimJson(unit2Dialogues);
        } else {
            System.out.println("Dialogue file not found!");
            System.out.println(prefix + "/NPC-Dialogue/Unit_2_Dialogue.json");
        }
    }

    /**
     * Organizes the dialogues into different categories based on the actor.
     *
     * @param dialogues the array of dialogues to be organized.
     */
    private void skimJson(Dialogue[] dialogues) {
        for (Dialogue dialogue : dialogues) {
            switch (dialogue.getActor()) {
                case "Serenity":
                    serenityDialogues.add(dialogue);
                    break;
                case "Victoria":
                    victoriaDialogues.add(dialogue);
                    break;
                case "Zion":
                    zionDialogues.add(dialogue);
                    break;
                case "Mayor Goodway":
                    mayorDialogues.add(dialogue);
                    break;
                case "Santiago":
                    santiagoDialogues.add(dialogue);
                    break;
                case "U1 Tutorial":
                    u1TutorialDialogues.add(dialogue);
                    break;
                case "U2 Tutorial":
                    u2TutorialDialogues.add(dialogue);
                    break;
                default:
                    System.out.println("Dialogue actor not found: " + dialogue.getActor());
                    break;
            }
        }

        System.out.println("Serenity: " + serenityDialogues.size());
        System.out.println("Victoria: " + victoriaDialogues.size());
        System.out.println("Zion: " + zionDialogues.size());
        System.out.println("Mayor: " + mayorDialogues.size());
        System.out.println("Santiago: " + santiagoDialogues.size());
    }

    /**
     * Retrieves the dialogues for a specific actor.
     *
     * @param actor the name of the actor.
     * @return an array of dialogues for the specified actor.
     */
    public Dialogue[] getDialogue(String actor) {
        switch (actor) {
            case "Serenity":
                return serenityDialogues.toArray(new Dialogue[serenityDialogues.size()]);
            case "Victoria":
                return victoriaDialogues.toArray(new Dialogue[victoriaDialogues.size()]);
            case "Zion":
                return zionDialogues.toArray(new Dialogue[zionDialogues.size()]);
            case "Mayor Goodway":
                return mayorDialogues.toArray(new Dialogue[mayorDialogues.size()]);
            case "Santiago":
                return santiagoDialogues.toArray(new Dialogue[santiagoDialogues.size()]);
            case "U1 Tutorial":
                return u1TutorialDialogues.toArray(new Dialogue[u1TutorialDialogues.size()]);
            case "U2 Tutorial":
                return u2TutorialDialogues.toArray(new Dialogue[u2TutorialDialogues.size()]);
            default:
                System.out.println("Dialogue actor not found: " + actor);
                return null;
        }
    }

    /**
     * Runs a dialogue for a specific actor, player, unit, and ID.
     *
     * @param actor  the name of the actor.
     * @param player the player to whom the dialogue is run.
     * @param unit   the unit of the dialogue.
     * @param id     the ID of the dialogue.
     */
    public void runDialogue(String actor, Player player, String unit, String id) {
        if (actor == null) return;
        if (actor.equals("T1")) {
            Dialogue[] dialogue = getDialogue("U1 Tutorial");

            if (id == null) id = "0.0";

            for (int i = 0; i < dialogue.length; i++) {
                if (dialogue[i].getId().equals(id) && dialogue[i].getUnit().equals(unit)) {

                    if (i < dialogue.length) {
                        // Send dialogue[i + dialogueIndex] to player

                        String[] speech = dialogue[i].getDialog();
                        String[] response = dialogue[i].getResponse();

                        Component bookTitle = Component.text("");
                        Component bookAuthor = Component.text("");

                        ArrayList<Component> pages = new ArrayList<>();
                        for (int j = 0; j < speech.length - 1; j++) {
                            String s = speech[j];
                            pages.add(Component.text(s));
                        }

                        TextComponent lastPage = Component.text(speech[speech.length - 1]);
                        for (int j = 0; j < response.length; j++) {
                            lastPage = lastPage.append(
                                    Component.text("\n" + response[j])
                                            .color(NamedTextColor.DARK_GREEN)
                                            .decorate(TextDecoration.BOLD)
                                            .clickEvent(ClickEvent.runCommand("/setstage " + unit + " " + dialogue[i].trigger[j]))
                            );
                        }

                        pages.add(lastPage);
                        Collection<Component> bookPages = pages;
                        Book book = Book.book(bookTitle, bookAuthor, bookPages);
                        plugin.adventure().player(player).openBook(book);
                    }
                    break;
                }
            }
        } else if (actor.equals("T2")) {
            Dialogue[] dialogue = getDialogue("U2 Tutorial");

            if (id == null) id = "0.0";

            for (int i = 0; i < dialogue.length; i++) {
                if (dialogue[i].getId().equals(id) && dialogue[i].getUnit().equals(unit)) {

                    if (i < dialogue.length) {
                        // Send dialogue[i + dialogueIndex] to player
                        String[] speech = dialogue[i].getDialog();
                        String[] response = dialogue[i].getResponse();

                        Component bookTitle = Component.text("");
                        Component bookAuthor = Component.text("");

                        ArrayList<Component> pages = new ArrayList<>();
                        for (int j = 0; j < speech.length - 1; j++) {
                            String s = speech[j];
                            pages.add(Component.text(s));
                        }

                        TextComponent lastPage = Component.text(speech[speech.length - 1]);
                        for (int j = 0; j < response.length; j++) {
                            lastPage = lastPage.append(
                                    Component.text("\n" + response[j])
                                            .color(NamedTextColor.DARK_GREEN)
                                            .decorate(TextDecoration.BOLD)
                                            .clickEvent(ClickEvent.runCommand("/setstage " + unit + " " + dialogue[i].trigger[j]))
                            );
                        }

                        pages.add(lastPage);
                        Collection<Component> bookPages = pages;
                        Book book = Book.book(bookTitle, bookAuthor, bookPages);
                        plugin.adventure().player(player).openBook(book);
                        break;
                    }
                }
            }
        }
    }
}

