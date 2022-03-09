package datastruct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnsortedListTest {
	
	static MyUnsortedList<Integer> l0;
	static MyUnsortedList<Integer> l1;
	static MyUnsortedList<Integer> l2;
	
	<E> void assertIntegrity(MyUnsortedList<E> l, String operation) {
		int r = l.isIntegrityRespected();
		switch (r) {
		case -1:
			fail(operation + " à casser le chainage de la liste");
			break;
		case -2:
			fail(operation + " à donner un precedent a la tete");
			break;
		case -3:
			fail(operation + " à donner un suivant a la queue");
			break;
		case -4:
			fail(operation + " à rendu null la tete ou la queue, mais pas l'autre");
			break;
		default:
			if(r != l.size())
				fail(operation + " à casser la taille de la liste ( " + r + " au lieu de " + l.size() + ")");
			break;
		}
	}
	
	<E> void assertUnequalsList(UnsortedList<E> l1, UnsortedList<E> l2) {
		if(l1.equals(l2)) {
			fail("" + l1 + " et " + l2 + " ne devraient pas etre egales");
		}
	}
	
	@BeforeEach
	private void init() {
		l0 = MyUnsortedList.of();
		l1 = MyUnsortedList.of(2);
		l2 = MyUnsortedList.of(2,5,8,2,8,4);
	}

	@Test
	void empty_void_init() {
		assertTrue("Liste cree vide est non vide", l0.isEmpty());
		l0.append(1);
		assertFalse("Liste vide apres l'ajout d'un element", l0.isEmpty());
		l0.remove(0);
		assertTrue("Liste non vide apres la suppresion d'un element", l0.isEmpty());
	}
	
	@Test
	void empty_single_init() {
		assertFalse("Liste cree non vide est vide", l1.isEmpty());
		l1.remove(0);
		assertTrue("Liste non vide apres la suppresion d'un element", l1.isEmpty());
	}
	

	
	@Test
	void empty_complex_pop() {
		assertFalse("Liste cree non vide est vide", l2.isEmpty());
		l2.remove(0);
		assertFalse("Liste vide apres la suppresion d'un element", l2.isEmpty());
		l2.pop();
		l2.popLast();
		l2.pop();
		l2.remove(1);
		assertFalse("Liste vide apres la suppresion de nombreux éléments", l2.isEmpty());
		l2.pop();
		assertTrue("Liste non vide apres la suppresion d'un element", l2.isEmpty());
	}
	
	@Test
	void empty_complex_popLast() {
		assertFalse("Liste cree non vide est vide", l2.isEmpty());
		l2.remove(3);
		assertFalse("Liste vide apres la suppresion d'un element", l2.isEmpty());
		l2.popLast();
		l2.pop();
		l2.pop();
		l2.remove(1);
		assertFalse("Liste vide apres la suppresion de nombreux éléments", l2.isEmpty());
		l2.popLast();
		assertTrue("Liste non vide apres la suppresion d'un element", l2.isEmpty());
	}
	
	@Test
	void empty_prepend() {
		l0.prepend(1);
		assertEqualsList(l0, MyUnsortedList.of(1), "prepend 1");
	}
	
	@Test
	void empty_append() {
		l0.prepend(1);
		assertEqualsList(l0, MyUnsortedList.of(1), "append 1");
	}
	
	<E> void sizeAssert(UnsortedList<E> l, int s, String operation) {
		assertEquals("Liste de mauvaise taille apres un " + operation, s, l.size());
	}
	
	@Test
	void size_test() {
		assertEquals("Liste de mauvaise taille à l'initialisation", 0, l0.size());
		assertEquals("Liste de mauvaise taille à l'initialisation", 1, l1.size());
		assertEquals("Liste de mauvaise taille à l'initialisation", 6, l2.size());
		
		l2.append(5);
		sizeAssert(l2, 7, "append");
		l2.prepend(8);
		sizeAssert(l2, 8, "prepend");
		l2.pop();
		sizeAssert(l2, 7, "pop");
		l2.popLast();
		sizeAssert(l2, 6, "popLast");
		l2.insert(4, 0);
		sizeAssert(l2, 7, "insert au debut");
		l2.insert(5, 7);
		sizeAssert(l2, 8, "insert a la fin");
		l2.insert(1, 5);
		sizeAssert(l2, 9, "insert au milieu");
		l2.remove(0);
		sizeAssert(l2, 8, "remove au debut");
		l2.remove(7);
		sizeAssert(l2, 7, "remove a la fin");
		l2.remove(2);
		sizeAssert(l2, 6, "remove au milieu");
		
		l1.pop();
		assertEquals("Liste de mauvaise taille à la suppression du dernier element", 0, l1.size());
	}
	
	<E> void remove_empty_test(UnsortedList<E> l, int... indexs) {
		for(int i : indexs) {
			l.remove(i);
		}
		assertTrue("Liste non vide apres la suppresion des elements", l.isEmpty());
	}
	
	@Test
	void remove_head() {
		remove_empty_test(l2, 0,0,0,0,0,0);
	}
	
	@Test
	void remove_tail() {
		remove_empty_test(l2, 5,4,3,2,1,0);
	}
	
	@Test
	void remove_spread1() {
		remove_empty_test(l2, 3,2,3,0,1,0);
	}
	
	@Test
	void remove_spread2() {
		remove_empty_test(l2, 5,0,2,1,0,0);
	}
	<E> void removeOFB(UnsortedList<E> l, int index) {
		try {
			l.remove(index);
			fail("Suppresion OutOfBound Réussit");
		} catch (IndexOutOfBoundsException e) {
		}
	}
	
	@Test
	void removeIndexOutOfBounds() {
		removeOFB(l0, 0);
		removeOFB(l1, -1);
		removeOFB(l1, 1);
		removeOFB(l2, 6);
		removeOFB(l2, 18);
		removeOFB(l2, -58);
		try {
			l2.remove(5);
		} catch(IndexOutOfBoundsException e) {
			fail("Suppresion OutOfBound Echoué a cause d'autres echecs precedents");
		}
	}

	<E> void insertOFB(UnsortedList<E> l, int index) {
		try {
			l.insert(null,index);
			fail("Insertion OutOfBound Réussit");
		} catch (IndexOutOfBoundsException e) {
		}
	}
	
	@Test
	void insertIndexOutOfBounds() {
		insertOFB(l0, 1);
		insertOFB(l1, -1);
		insertOFB(l1, 2);
		insertOFB(l2, 7);
		insertOFB(l2, 7);
		insertOFB(l2, 7);
		insertOFB(l2, 9);
		insertOFB(l2, -5);
	}
	
	<E> void assertPopEmpty(UnsortedList<E> l) {
		try {
			l.pop();
			fail("Pop dans une liste vide Réussit");
		} catch (EmptyListException e) {
		}
	}
	
	<E> void assertPopLastEmpty(UnsortedList<E> l) {
		try {
			l.popLast();
			fail("Pop dans une liste vide Réussit");
		} catch (EmptyListException e) {
		}
	}
	
	
	@Test
	void emptyList() {
		assertPopEmpty(l0);
		assertPopLastEmpty(l0);
		l1.pop();
		assertPopEmpty(l1);
		assertPopLastEmpty(l1);
		l1.append(1);
		l1.pop();
		assertPopEmpty(l1);
		assertPopLastEmpty(l1);
	}
	
	<E> void assertEqualsList(UnsortedList<E> l1, UnsortedList<E> l2, String operation) {
		if(!l1.equals(l2)) {
			fail("" + l1 + " et " + l2 + " devraient etre egals apres " + operation);
		}
	}
	
	@Test
	void equalsTest() {
		assertUnequalsList(l0, l1);
		l0.append(2);
		assertEqualsList(l0, l1, "append 2");
		l1.append(5);
		l1.append(2);
		l2.popLast();
		l2.popLast();
		l2.remove(2);
		assertEqualsList(l1, l2, "append 5,2 (l1), popLast*2, remove 2 (l2)");
		l2.popLast();
		l2.append(5);
		assertUnequalsList(l1, l2);
	}
	
	@Test
	void integrityTest() {
		assertIntegrity(l2, "aucune manipulation");
		assertEqualsList(l2, MyUnsortedList.of(2,5,8,2,8,4), "aucune manipulation");
		l2.append(7);
		assertIntegrity(l2, "append 7");
		assertEqualsList(l2, MyUnsortedList.of(2,5,8,2,8,4,7), "append 7");
		l2.append(10);
		assertIntegrity(l2, "append 10");
		assertEqualsList(l2, MyUnsortedList.of(2,5,8,2,8,4,7,10), "append 10");
		l2.remove(5);
		assertIntegrity(l2, "remove 5");
		assertEqualsList(l2, MyUnsortedList.of(2,5,8,2,8,7,10), "remove 5");
		l2.pop();
		assertIntegrity(l2, "pop");
		assertEqualsList(l2, MyUnsortedList.of(5,8,2,8,7,10), "pop");
		l2.popLast();
		assertIntegrity(l2, "popLast");
		assertEqualsList(l2, MyUnsortedList.of(5,8,2,8,7), "popLast");
		l2.prepend(8);
		assertIntegrity(l2, "prepend 8");
		assertEqualsList(l2, MyUnsortedList.of(8,5,8,2,8,7), "prepend 8");
		l2.append(5);
		assertIntegrity(l2, "append 5");
		assertEqualsList(l2, MyUnsortedList.of(8,5,8,2,8,7,5), "append 5");
		l2.remove(1);
		assertIntegrity(l2, "remove 1");
		assertEqualsList(l2, MyUnsortedList.of(8,8,2,8,7,5), "remove 1");
		l2.remove(1);
		assertIntegrity(l2, "remove 1");
		assertEqualsList(l2, MyUnsortedList.of(8,2,8,7,5), "remove 1");
		l2.remove(1);
		assertIntegrity(l2, "remove 1");
		assertEqualsList(l2, MyUnsortedList.of(8,8,7,5), "remove 1");
		l2.remove(2);
		assertIntegrity(l2, "remove 2");
		assertEqualsList(l2, MyUnsortedList.of(8,8,5), "remove 2");
		l2.remove(0);
		assertIntegrity(l2, "remove 0");
		assertEqualsList(l2, MyUnsortedList.of(8,5), "remove 0");
		l2.append(3);
		assertIntegrity(l2, "append 3");
		assertEqualsList(l2, MyUnsortedList.of(8,5,3), "append 3");
		l2.append(6);
		assertIntegrity(l2, "append 6");
		assertEqualsList(l2, MyUnsortedList.of(8,5,3,6), "append 6");
		l2.pop();
		assertIntegrity(l2, "pop");
		assertEqualsList(l2, MyUnsortedList.of(5,3,6), "pop");
		l2.pop();
		assertIntegrity(l2, "pop");
		assertEqualsList(l2, MyUnsortedList.of(3,6), "pop");
		l2.popLast();
		assertIntegrity(l2, "popLast");
		assertEqualsList(l2, MyUnsortedList.of(3), "popLast");
		l2.insert(5,0);
		assertIntegrity(l2, "insert 5 0");
		assertEqualsList(l2, MyUnsortedList.of(5,3), "insert 5 0");
		l2.insert(4,2);
		assertIntegrity(l2, "insert 4 2");
		assertEqualsList(l2, MyUnsortedList.of(5,3,4), "insert 4 2");
		l2.insert(8,2);
		assertIntegrity(l2, "insert 8 2");
		assertEqualsList(l2, MyUnsortedList.of(5,3,8,4), "insert 8 2");
	}
	
}
