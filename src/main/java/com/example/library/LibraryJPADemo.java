package com.example.library;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class LibraryJPADemo {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("LibraryPU");
        EntityManager em = emf.createEntityManager();

        try {
            // Thêm sách mới (kiểm tra ISBN trước)
            String isbn = "12345";
            if (!isISBNExists(em, isbn)) {
                em.getTransaction().begin();
                Book book = new Book("Java Programming", "John Doe", isbn, true);
                em.persist(book);
                em.getTransaction().commit();
                System.out.println("Added new book using JPA.");
            } else {
                System.out.println("Book with ISBN " + isbn + " already exists.");
            }

            // Minh họa giao dịch mượn sách
            int bookID = getBookIDByISBN(em, isbn);
            int memberID = 2;
            if (bookID != -1 && isMemberExists(em, memberID)) {
                borrowBookTransaction(em, bookID, memberID);
            } else {
                System.out.println("Cannot borrow: Book or Member does not exist or book is not available.");
            }

            // Hiển thị tất cả sách
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b", Book.class);
            for (Book book : query.getResultList()) {
                System.out.println("Book: " + book.getTitle() + ", Author: " + book.getAuthor() +
                        ", ISBN: " + book.getIsbn() + ", Available: " + book.isAvailable());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    private static boolean isISBNExists(EntityManager em, String isbn) {
        Query query = em.createQuery("SELECT COUNT(b) FROM Book b WHERE b.isbn = :isbn");
        query.setParameter("isbn", isbn);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

    private static int getBookIDByISBN(EntityManager em, String isbn) {
        try {
            Query query = em.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbn AND b.isAvailable = true");
            query.setParameter("isbn", isbn);
            Book book = (Book) query.getSingleResult();
            return book.getBookID();
        } catch (NoResultException e) {
            return -1; // Sách không tồn tại hoặc không có sẵn
        }
    }

    private static boolean isMemberExists(EntityManager em, int memberID) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM Members WHERE MemberID = ?");
        query.setParameter(1, memberID);
        Long count = ((Number) query.getSingleResult()).longValue();
        return count > 0;
    }

    private static void borrowBookTransaction(EntityManager em, int bookID, int memberID) {
        em.getTransaction().begin();
        try {
            // Tìm sách
            Book book = em.find(Book.class, bookID);
            if (book == null || !book.isAvailable()) {
                throw new IllegalStateException("Book is not available or does not exist.");
            }

            // Cập nhật trạng thái sách
            book.setAvailable(false);

            // Thêm bản ghi mượn
            BorrowRecord record = new BorrowRecord(book, memberID, LocalDate.of(2025, 5, 13));
            em.persist(record);

            em.getTransaction().commit();
            System.out.println("Book borrowed successfully.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Transaction failed: " + e.getMessage());
            throw e;
        }
    }
}
