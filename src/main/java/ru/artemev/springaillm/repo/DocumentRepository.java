package ru.artemev.springaillm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.artemev.springaillm.model.LoadedDocument;

@Repository
public interface DocumentRepository extends JpaRepository<LoadedDocument, Long> {

    boolean existsByFileNameAndContentHash(String fileName, String contentHash);

}
