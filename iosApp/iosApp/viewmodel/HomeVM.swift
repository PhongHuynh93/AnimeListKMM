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
            var index = 0
            var tempHomeList = [HomeUI]()
            list!.forEach { it in
                tempHomeList.append(HomeUI(pos: index, home: it as! Home))
                index += 1
            }
            self.homeList = tempHomeList
        }
    }
}
