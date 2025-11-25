package com.maksymchernenko.cinebook.service.impl;

import com.maksymchernenko.cinebook.exception.NotFoundException;
import com.maksymchernenko.cinebook.model.*;
import com.maksymchernenko.cinebook.repository.HallRepository;
import com.maksymchernenko.cinebook.repository.ScreeningRepository;
import com.maksymchernenko.cinebook.repository.ScreeningSeatRepository;
import com.maksymchernenko.cinebook.repository.SeatRepository;
import com.maksymchernenko.cinebook.service.MovieService;
import com.maksymchernenko.cinebook.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    Map<Seat.Type, BigDecimal> pricesUAH = new HashMap<>();
    {
        pricesUAH.put(Seat.Type.REGULAR, BigDecimal.valueOf(100));
        pricesUAH.put(Seat.Type.VIP, BigDecimal.valueOf(180));
        pricesUAH.put(Seat.Type.ACCESSIBLE, BigDecimal.valueOf(75));
    }

    private final ScreeningRepository screeningRepository;
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;
    private final ScreeningSeatRepository screeningSeatRepository;

    private final MovieService movieService;

    @Autowired
    public ScreeningServiceImpl(ScreeningRepository screeningRepository,
                                HallRepository hallRepository,
                                SeatRepository seatRepository,
                                ScreeningSeatRepository screeningSeatRepository,
                                MovieService movieService) {
        this.screeningRepository = screeningRepository;
        this.hallRepository = hallRepository;
        this.seatRepository = seatRepository;
        this.screeningSeatRepository = screeningSeatRepository;

        this.movieService = movieService;
    }

    // Screening API

    @Override
    public Page<Screening> getScreeningsByMovieAndDate(Integer movieId, LocalDate date, Pageable pageable) {
        Movie movie = movieService.getMovieById(movieId);

        return screeningRepository.findScreeningsByMovie_AndStartTime_Date(movie, date, pageable);
    }

    @Override
    public Screening getScreeningById(Integer id) {
        return screeningRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Screening with id = %d not found", id)));
    }

    @Override
    public Page<Screening> getAllScreenings(Pageable pageable) {
        return screeningRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Screening createScreening(Screening screening) {
        if (screening.getMovie() == null || screening.getMovie().getId() == null) {
            throw new IllegalArgumentException("Screening must contain a movie with id");
        }

        if (screening.getHall() == null || screening.getHall().getId() == null) {
            throw new IllegalArgumentException("Screening must contain a hall with id");
        }

        Movie movie = movieService.getMovieById(screening.getMovie().getId());
        Hall hall = hallRepository.findById(screening.getHall().getId())
                .orElseThrow(() -> new NotFoundException(String.format("Hall with id = %d not found", screening.getHall().getId())));

        List<Seat> seats = Optional.ofNullable(hall.getSeats()).orElse(Collections.emptyList());
        if (seats.isEmpty()) {
            throw new IllegalStateException("Hall has no seats to create screening seats from");
        }

        screening.setId(null);
        screening.setMovie(movie);
        screening.setHall(hall);

        Screening saved = screeningRepository.save(screening);

        List<ScreeningSeat> screeningSeats = seats.stream()
                .map(seat -> {
                    BigDecimal priceAmount = pricesUAH.get(seat.getType());
                    if (priceAmount == null) throw new IllegalStateException("No price configured for seat type " + seat.getType());
                    return ScreeningSeat.builder()
                            .seat(seat)
                            .screening(saved)
                            .price(Price.builder()
                                    .currency(Price.Currency.UAH)
                                    .amount(priceAmount)
                                    .build())
                            .status(ScreeningSeat.Status.AVAILABLE)
                            .build();
                })
                .collect(Collectors.toList());

        screeningSeatRepository.saveAll(screeningSeats);

        return saved;
    }

    @Override
    @Transactional
    public Screening updateScreening(Screening screening) {
        if (screening.getId() == null) {
            throw new IllegalArgumentException("Screening id must be provided for update");
        }
        Screening existing = screeningRepository.findById(screening.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Screening with id = %d not found", screening.getId())));

        if (screening.getMovie() != null && screening.getMovie().getId() != null) {
            Movie movie = movieService.getMovieById(screening.getMovie().getId());
            existing.setMovie(movie);
        }

        if (screening.getHall() != null && screening.getHall().getId() != null) {
            Hall hall = hallRepository.findById(screening.getHall().getId())
                    .orElseThrow(() -> new NotFoundException(String.format("Hall with id = %d not found", screening.getHall().getId())));
            existing.setHall(hall);
        }

        if (screening.getStartTime() != null) {
            existing.setStartTime(screening.getStartTime());
        }

        return screeningRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteScreeningById(Integer screeningId) {
        screeningSeatRepository.deleteByScreeningId(screeningId);
        screeningRepository.deleteById(screeningId);
    }

    // Hall API

    @Override
    public Hall getHallByScreeningId(Integer screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new NotFoundException(String.format("Screening with id = %d not found", screeningId)));
        Integer hallId = screening.getHall().getId();

        return hallRepository.findById(hallId)
                .orElseThrow(() -> new NotFoundException(String.format("Hall with id = %d not found", hallId)));
    }

    @Override
    public Hall getHallById(Integer id) {
        return hallRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Hall with id = %d not found", id)));
    }

    @Override
    public Page<Hall> getAllHalls(Pageable pageable) {
        return hallRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Hall createHall(Hall hall) {
        hall.setId(null);

        return hallRepository.save(hall);
    }

    @Override
    @Transactional
    public Hall updateHall(Hall hall) {
        return hallRepository.save(hall);
    }

    @Override
    @Transactional
    public void deleteHallById(Integer id) {
        hallRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Hall addSeatByHallId(Integer hallId, Integer seatId) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new NotFoundException(String.format("Hall with id = %d not found", hallId)));
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new NotFoundException(String.format("Seat with id = %d not found", hallId)));

        hall.getSeats().add(seat);

        return hallRepository.save(hall);
    }

    @Override
    @Transactional
    public void removeSeatByHallId(Integer hallId, Integer seatId) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new NotFoundException(String.format("Hall with id = %d not found", hallId)));
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new NotFoundException(String.format("Seat with id = %d not found", hallId)));

        hall.getSeats().remove(seat);

        hallRepository.save(hall);
    }

    // Seat API

    @Override
    public List<Seat> getSeatsByHallId(Integer hallId) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new NotFoundException(String.format("Hall with id = %d not found", hallId)));

        return hall.getSeats();
    }

    @Override
    public List<ScreeningSeat> getSeatsByScreeningAndLockToken(Integer screeningId, String lockToken) {
        return screeningSeatRepository.findAllByScreening_IdAndLockToken(screeningId, lockToken);
    }

    @Override
    @Transactional
    public Seat createSeat(Seat seat) {
        if (seat == null) throw new IllegalArgumentException("Seat must not be null");
        if (seat.getHall() == null || seat.getHall().getId() == null) {
            throw new IllegalArgumentException("Seat must contain hallId");
        }

        Integer hallId = seat.getHall().getId();
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new NotFoundException(String.format("Hall with id = %d not found", hallId)));

        seat.setId(null);
        seat.setHall(hall);

        return seatRepository.save(seat);
    }

    @Override
    @Transactional
    public Seat updateSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    @Override
    @Transactional
    public void deleteSeatById(Integer id) {
        seatRepository.deleteById(id);
    }

    @Override
    @Transactional
    public String lockSeats(Integer screeningId, List<Integer> seatIds) {
        screeningRepository.findById(screeningId)
                .orElseThrow(() -> new NotFoundException(String.format("Screening with id = %d not found", screeningId)));

        List<ScreeningSeat> seats = screeningSeatRepository.findByScreeningIdAndSeatIdInForUpdate(screeningId, seatIds);

        List<Integer> foundIds = seats.stream()
                .map(s -> s.getSeat().getId())
                .toList();

        List<Integer> missing = seatIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missing.isEmpty()) {
            throw new NotFoundException(String.format("Seats not found for ids: %s in screening %d", missing, screeningId));
        }

        List<ScreeningSeat> unavailable = seats.stream()
                .filter(s -> !s.getStatus().equals(ScreeningSeat.Status.AVAILABLE))
                .toList();

        if (!unavailable.isEmpty()) {
            throw new IllegalStateException("Some seats are not available: " + unavailable.stream()
                            .map(s -> s.getSeat().getId().toString())
                            .collect(Collectors.joining(", ")));
        }

        String token = UUID.randomUUID().toString();
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(LOCKING_TIME_IN_SECONDS);

        seats.forEach(s -> {
                    s.setLockToken(token);
                    s.setLockedAt(now);
                    s.setExpiresAt(expiresAt);
                    s.setStatus(ScreeningSeat.Status.LOCKED);
                });

        screeningSeatRepository.saveAll(seats);

        return token;
    }

    @Override
    @Transactional
    public void confirmSeats(Integer screeningId, String lockToken) {
        screeningRepository.findById(screeningId)
                .orElseThrow(() -> new NotFoundException(String.format("Screening with id = %d not found", screeningId)));

        List<ScreeningSeat> lockedSeats = screeningSeatRepository.findAllByScreening_IdAndLockToken(screeningId, lockToken);

        if (lockedSeats.isEmpty()) {
            throw new NotFoundException(String.format("No locked seats found for screening id = %d and token = %s", screeningId, lockToken));
        }

        Instant now = Instant.now();
        boolean anyExpired = lockedSeats.stream().anyMatch(s -> s.getExpiresAt() != null && s.getExpiresAt().isBefore(now));
        if (anyExpired) {
            lockedSeats.forEach(s -> {
                s.setStatus(ScreeningSeat.Status.AVAILABLE);
                s.setLockToken(null);
                s.setLockedAt(null);
                s.setExpiresAt(null);
            });

            screeningSeatRepository.saveAll(lockedSeats);

            throw new IllegalStateException("Some locks have expired, seats were unlocked.");
        }

        lockedSeats.forEach(s -> {
            s.setStatus(ScreeningSeat.Status.SOLD);
            s.setLockToken(null);
            s.setLockedAt(null);
            s.setExpiresAt(null);
        });

        screeningSeatRepository.saveAll(lockedSeats);
    }

    @Override
    @Transactional
    public void unlockSeats(Integer screeningId, String lockToken) {
        screeningRepository.findById(screeningId)
                .orElseThrow(() -> new NotFoundException(String.format("Screening with id = %d not found", screeningId)));

        List<ScreeningSeat> lockedSeats = screeningSeatRepository.findAllByScreening_IdAndLockToken(screeningId, lockToken);

        if (lockedSeats.isEmpty()) {
            throw new NotFoundException(String.format("No locked seats found for screening id = %d and token = %s", screeningId, lockToken));
        }

        lockedSeats.forEach(s -> {
            s.setStatus(ScreeningSeat.Status.AVAILABLE);
            s.setLockToken(null);
            s.setLockedAt(null);
            s.setExpiresAt(null);
        });

        screeningSeatRepository.saveAll(lockedSeats);
    }
}
