package ru.practicum.ewm.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.ewm.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long>, PagingAndSortingRepository<Compilation, Long> {
    public List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}