//
//  HomeVM.swift
//  iosApp
//
//  Created by Coder on 10/14/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

class HomeVM: ObservableObject {
    @Published var homeList: [HomeUI]
    
    init(homeVM: HomeViewModel) {
        homeList = [HomeUI]()
        homeVM.data.watch { list in
            var indexOuter = 0
            var tempHomeList = [HomeUI]()
            list!.forEach { it in
                switch it {
                case is MangaList:
                    // convert nsarray into array
                    var indexInner = 0
                    var mangaList = [MangaUI]()
                    (it as! MangaList).list.forEach { it in
                        mangaList.append(MangaUI(pos: indexInner, manga: it))
                        indexInner += 1
                    }
                    
                    tempHomeList.append(HomeUI(pos: indexOuter, home: MangaListUI(mangaList: mangaList)))
                default:
                    tempHomeList.append(HomeUI(pos: indexOuter, home: it as! Home))
                }
                indexOuter += 1
               
            }
            self.homeList = tempHomeList
        }
    }
}
