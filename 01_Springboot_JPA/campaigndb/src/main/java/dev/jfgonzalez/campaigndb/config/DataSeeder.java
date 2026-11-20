package dev.jfgonzalez.campaigndb.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import dev.jfgonzalez.campaigndb.model.Campaign;
import dev.jfgonzalez.campaigndb.model.GameCharacter;
import dev.jfgonzalez.campaigndb.model.Player;
import dev.jfgonzalez.campaigndb.repository.CampaignRepository;
import dev.jfgonzalez.campaigndb.repository.GameCharacterRepository;
import dev.jfgonzalez.campaigndb.repository.PlayerRepository;
import net.datafaker.Faker;

@Component
public class DataSeeder implements CommandLineRunner {

    private final PlayerRepository playerRepository;
    private final CampaignRepository campaignRepository;
    private final GameCharacterRepository gameCharacterRepository;

    public DataSeeder(
        PlayerRepository playerRepository,
        CampaignRepository campaignRepository,
        GameCharacterRepository gameCharacterRepository
    ) {
        this.playerRepository = playerRepository;
        this.campaignRepository = campaignRepository;
        this.gameCharacterRepository = gameCharacterRepository;
    }

    @Override
    public void run(String... args) {
        if (campaignRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();

        List<Player> players = playerRepository.findAll();
        if (players.isEmpty()) {
            players = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Player player = new Player();
                player.setName(faker.name().fullName());
                player.setEmail(faker.internet().emailAddress());
                player.setAdmin(i == 0);
                players.add(playerRepository.save(player));
            }
        }

        String[] systems = { "D&D 5e", "Pathfinder 2e", "Call of Cthulhu", "Vampire: The Masquerade", "Cyberpunk RED" };
        String[] statuses = { "WIP", "ACTIVE", "PAUSED", "FINISHED" };

        List<Campaign> campaigns = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            Campaign campaign = new Campaign();
            campaign.setName(faker.book().title());
            campaign.setDescription(faker.lorem().paragraph());
            campaign.setGameSystem(faker.options().option(systems));
            campaign.setStatus(faker.options().option(statuses));
            campaign.setSecondsPlayed(faker.number().numberBetween(0, 200_000));
            campaign.setOwner(players.get(faker.number().numberBetween(0, players.size())));
            campaign.setProperties(new HashMap<>(Map.of(
                "edition", faker.number().numberBetween(1, 5),
                "homebrew", faker.bool().bool()
            )));
            campaigns.add(campaignRepository.save(campaign));
        }

        for (int i = 0; i < 50; i++) {
            GameCharacter character = new GameCharacter();
            character.setName(faker.name().fullName());
            character.setBackground(faker.lorem().paragraph());
            character.setAvatarUrl("https://api.dicebear.com/9.x/adventurer/svg?seed=" + i);
            character.setPc(faker.bool().bool());
            character.setLevel(faker.number().numberBetween(1, 20));
            character.setOwner(players.get(faker.number().numberBetween(0, players.size())));

            if (i % 5 != 0) {
                character.setCampaign(campaigns.get(faker.number().numberBetween(0, campaigns.size())));
            }

            character.setProperties(new HashMap<>(Map.of(
                "hp", faker.number().numberBetween(10, 150),
                "gold", faker.number().numberBetween(0, 5000)
            )));
            gameCharacterRepository.save(character);
        }
    }
}