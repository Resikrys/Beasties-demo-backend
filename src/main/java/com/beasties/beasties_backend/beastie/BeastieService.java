package com.beasties.beasties_backend.beastie;

import com.beasties.beasties_backend.beastie.dto.BeastieCreationDTO;
import com.beasties.beasties_backend.beastie.dto.BeastieDTO;
import com.beasties.beasties_backend.exception.ResourceNotFoundException;
import com.beasties.beasties_backend.user.User;
import com.beasties.beasties_backend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BeastieService {

    private final BeastieRepository beastieRepository;
    private final UserRepository userRepository;
    private final BeastieMapper beastieMapper;
    private static final int MAX_TEAM_SIZE = 5;
    private final Random random = new Random();

    public BeastieService(
            BeastieRepository beastieRepository,
            UserRepository userRepository,
            BeastieMapper beastieMapper) {
        this.beastieRepository = beastieRepository;
        this.userRepository = userRepository;
        this.beastieMapper = beastieMapper;
    }

    private static final Map<BeastieType, List<String>> BEASTIE_IMAGES = Map.of(
            BeastieType.FIGHTER, List.of(
                    "/assets/images/beastie/tiny.png",
                    "/assets/images/beastie/rowley.png"
            ),
            BeastieType.EXPLORER, List.of(
                    "/assets/images/beastie/chubbs.png",
                    "/assets/images/beastie/dotts.png"
            ),
            BeastieType.SAGE, List.of(
                    "/assets/images/beastie/cosmo.png",
                    "/assets/images/beastie/goldie.png"
            )
    );

    // --- (USER & ADMIN) ---

    /**
     * Allows a User to adopt a Beastie (create).
     */
    public BeastieDTO adoptBeastie(BeastieCreationDTO dto, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Beastie newBeastie = new Beastie(dto.getName(), dto.getType(), owner);

        if (dto.getType() == BeastieType.FIGHTER) newBeastie.setStrength(3);
        if (dto.getType() == BeastieType.EXPLORER) newBeastie.setDexterity(3);
        if (dto.getType() == BeastieType.SAGE) newBeastie.setIntelligence(3);

        if (dto.getImageUrl() != null && !dto.getImageUrl().isEmpty()) {
            newBeastie.setImageUrl(dto.getImageUrl());
        } else {
            newBeastie.setImageUrl(selectRandomImageUrl(dto.getType()));
        }

        Beastie savedBeastie = beastieRepository.save(newBeastie);
        return beastieMapper.toDTO(savedBeastie);
    }

    /**
     * Select a random image URL based on the Beastie type.
     */
    private String selectRandomImageUrl(BeastieType type) {
        List<String> urls = BEASTIE_IMAGES.getOrDefault(type,
                List.of("/assets/images/beastie/default.png"));

        int randomIndex = random.nextInt(urls.size());

        return urls.get(randomIndex);
    }

    /**
     * getAll Beasties from a User.
     */
    public List<BeastieDTO> getMyBeasties(Long userId) {
        List<Beastie> beasties = beastieRepository.findByOwnerId(userId);
        return beasties.stream()
                .map(beastieMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Release (eliminate) a creature if it belongs to the user.
     */
    public void releaseBeastie(Long beastieId, Long userId) {
        Beastie beastie = beastieRepository.findByIdAndOwnerId(beastieId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Beastie", "id", beastieId));

        beastieRepository.delete(beastie);
    }

    /**
     * Add or remove a creature from the team (with size validation).
     */
    public BeastieDTO toggleTeam(Long beastieId, Long userId) {
        Beastie beastie = beastieRepository.findByIdAndOwnerId(beastieId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Beastie", "id", beastieId));

        if (!beastie.isInTeam()) {
            long currentTeamSize = beastieRepository.countByOwnerIdAndIsInTeamTrue(userId);
            if (currentTeamSize >= MAX_TEAM_SIZE) {
                throw new IllegalStateException("Your team has reached the full size of " + MAX_TEAM_SIZE + " beasties.");
            }
        }

        beastie.setInTeam(!beastie.isInTeam());
        Beastie updatedBeastie = beastieRepository.save(beastie);
        return beastieMapper.toDTO(updatedBeastie);
    }

    // --- (ADMIN) ---

    /**
     * getAll Beasties from all Users.
     */
    public List<BeastieDTO> getAllBeastiesForAdmin() {
        List<Beastie> beasties = beastieRepository.findAll();
        return beasties.stream()
                .map(beastieMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BeastieDTO renameBeastie(Long beastieId, String newName) {
        Beastie beastie = beastieRepository.findById(beastieId)
                .orElseThrow(() -> new ResourceNotFoundException("Beastie", "id", beastieId.toString()));
        beastie.setName(newName);
        Beastie updatedBeastie = beastieRepository.save(beastie);
        return beastieMapper.toDTO(updatedBeastie);
    }
}
